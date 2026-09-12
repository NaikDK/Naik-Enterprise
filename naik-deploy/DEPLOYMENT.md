================================================================================
NAIK ORG — DEPLOYMENT GUIDE
================================================================================

FOLDER STRUCTURE ON SERVER:
================================================================================

/opt/naik/
├── naik-mongodb/
│   ├── docker-compose.yml
│   └── .env
├── naik-auth/
│   ├── docker-compose.yml
│   └── .env
├── naik-trade-journal/
│   ├── docker-compose.yml
│   └── .env
└── naik-ui/
    └── docker-compose.yml

================================================================================
STEP 1 — BUILD JARS LOCALLY (on your development machine)
================================================================================

# Build common module first (shared dependency)
cd naik-apps/common
mvn clean install

# Build auth-service
cd naik-apps/auth-service
mvn clean package -DskipTests

# Build trade-journal
cd naik-apps/trade-journal
mvn clean package -DskipTests

================================================================================
STEP 2 — BUILD DOCKER IMAGES LOCALLY
================================================================================

# Build auth-service image
cd naik-apps/auth-service
docker build -t naik-auth:latest .

# Build trade-journal image
cd naik-apps/trade-journal
docker build -t naik-trade-journal:latest .

# Build React UI image
cd naik-apps/naik-trade-journal-ui
docker build -t naik-ui:latest .

================================================================================
STEP 3 — SAVE AND TRANSFER IMAGES TO SERVER
================================================================================

# Save images to tar files
docker save naik-auth:latest | gzip > naik-auth.tar.gz
docker save naik-trade-journal:latest | gzip > naik-trade-journal.tar.gz
docker save naik-ui:latest | gzip > naik-ui.tar.gz

# Transfer to server
scp naik-auth.tar.gz user@your-server:/opt/naik/naik-auth/
scp naik-trade-journal.tar.gz user@your-server:/opt/naik/naik-trade-journal/
scp naik-ui.tar.gz user@your-server:/opt/naik/naik-ui/

# On server — load images
docker load < /opt/naik/naik-auth/naik-auth.tar.gz
docker load < /opt/naik/naik-trade-journal/naik-trade-journal.tar.gz
docker load < /opt/naik/naik-ui/naik-ui.tar.gz

================================================================================
STEP 4 — DEPLOY IN ORDER
================================================================================

# 1. MongoDB (if not already running)
cd /opt/naik/naik-mongodb
cp .env.example .env
nano .env   # fill in credentials
docker compose up -d

# 2. Auth Service
cd /opt/naik/naik-auth
cp .env.example .env
nano .env   # fill in MongoDB URI and JWT secret
docker compose up -d

# 3. Trade Journal
cd /opt/naik/naik-trade-journal
cp .env.example .env
nano .env   # fill in MongoDB URI and JWT secret
docker compose up -d

# 4. React UI
cd /opt/naik/naik-ui
docker compose up -d

================================================================================
STEP 5 — CONFIGURE CADDY
================================================================================

# Add Caddyfile config to your existing Caddy setup
sudo nano /etc/caddy/Caddyfile

# Add the naikorg.home.arpa block from Caddyfile provided
# Then reload Caddy
sudo systemctl reload caddy

================================================================================
STEP 6 — VERIFY DEPLOYMENT
================================================================================

# Check all containers running
docker ps

# Check health of each service
curl http://localhost:8080/auth/api/health
curl http://localhost:1111/journal/api/trades/health
curl http://localhost:3000

# Check via domain
curl http://naikorg.home.arpa/auth/api/health
curl http://naikorg.home.arpa/journal/api/trades/health
curl http://naikorg.home.arpa

================================================================================
STEP 7 — UPDATING A SERVICE
================================================================================

# Example: Update trade-journal
# 1. Build new JAR locally
cd naik-apps/trade-journal
mvn clean package -DskipTests

# 2. Build new Docker image
docker build -t naik-trade-journal:latest .

# 3. Save and transfer
docker save naik-trade-journal:latest | gzip > naik-trade-journal.tar.gz
scp naik-trade-journal.tar.gz user@your-server:/opt/naik/naik-trade-journal/

# 4. On server — load and restart
docker load < naik-trade-journal.tar.gz
cd /opt/naik/naik-trade-journal
docker compose up -d --force-recreate

================================================================================
ENVIRONMENT VARIABLES SUMMARY
================================================================================

naik-mongodb/.env:
  MONGO_ROOT_USER=admin
  MONGO_ROOT_PASSWORD=<strong_password>
  MONGO_DATABASE=naik_org

naik-auth/.env:
  MONGODB_URI=mongodb://admin:<password>@naik-mongodb:27017/naik_org?authSource=admin
  JWT_SECRET=<minimum_32_char_secret>

naik-trade-journal/.env:
  MONGODB_URI=mongodb://admin:<password>@naik-mongodb:27017/naik_org?authSource=admin
  JWT_SECRET=<same_jwt_secret_as_auth>

================================================================================
NETWORK DIAGRAM
================================================================================

Internet/Homelab
      ↓
naikorg.home.arpa
      ↓
Caddy (bare metal)
      ├── /* ──────────────→ localhost:3000 (naik-ui)
      ├── /auth/* ─────────→ localhost:8080 (naik-auth)
      └── /journal/* ──────→ localhost:1111 (naik-trade-journal)
                ↓
           NaikNet (Docker)
                ├── naik-ui            port 3000→80
                ├── naik-auth          port 8080→8080
                ├── naik-trade-journal port 1111→1111
                └── naik-mongodb       no ports (internal only)

================================================================================
IMPORTANT NOTES
================================================================================

1. JWT_SECRET must be IDENTICAL in naik-auth and naik-trade-journal
   Different secrets = all tokens rejected = 403 on every request

2. MongoDB is NOT exposed to host — only accessible within NaikNet
   Connection string uses container name: naik-mongodb (not localhost)

3. Deploy order matters:
   naik-mongodb → naik-auth → naik-trade-journal → naik-ui

4. NaikNet must exist before deploying auth/journal/ui
   It's created by naik-mongodb's docker-compose.yml
   Other stacks reference it as external: true

================================================================================
