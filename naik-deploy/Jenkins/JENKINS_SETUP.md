================================================================================
JENKINS SETUP GUIDE — NAIK ORG
================================================================================

STEP 1 — DEPLOY JENKINS
================================================================================

# On your server
cd /opt/naik/naik-jenkins
docker compose up -d

# Get initial admin password
docker logs naik-jenkins 2>&1 | grep -A 3 "initial admin password"
# OR
docker exec naik-jenkins cat /var/jenkins_home/secrets/initialAdminPassword

================================================================================
STEP 2 — ACCESS JENKINS UI
================================================================================

Add to your Caddyfile:

jenkins.naikorg.home.arpa {
    reverse_proxy localhost:8090
}

Then visit: http://jenkins.naikorg.home.arpa
Enter the initial admin password from Step 1

================================================================================
STEP 3 — INITIAL SETUP
================================================================================

1. Click "Install suggested plugins" — wait for installation
2. Create admin user:
   Username: your_username
   Password: strong_password
   Email:    your_email
3. Set Jenkins URL: http://jenkins.naikorg.home.arpa
4. Click "Save and Finish"

================================================================================
STEP 4 — INSTALL REQUIRED PLUGINS
================================================================================

Go to: Manage Jenkins → Plugins → Available

Search and install:
  ✅ Git plugin               (pull from GitHub)
  ✅ Pipeline                 (Jenkinsfile support)
  ✅ GitHub Integration       (webhook support later)

Restart Jenkins after installation:
  http://jenkins.naikorg.home.arpa/restart

================================================================================
STEP 5 — CONFIGURE GITHUB CREDENTIALS
================================================================================

Go to: Manage Jenkins → Credentials → System → Global → Add Credentials

Option A — HTTPS with Personal Access Token (recommended):
  Kind:     Username with password
  Username: your-github-username
  Password: your-github-personal-access-token
  ID:       github-credentials        ← must match Jenkinsfile
  Description: GitHub Access Token

To create GitHub Personal Access Token:
  GitHub → Settings → Developer Settings → Personal Access Tokens
  → Generate new token (classic)
  → Select scope: repo (read access)
  → Copy token — save it, shown only once

================================================================================
STEP 6 — INSTALL MAVEN IN JENKINS
================================================================================

Go to: Manage Jenkins → Tools → Maven installations → Add Maven

Name:    Maven-3.9
Version: 3.9.6 (latest)
✅ Install automatically

Click Save

================================================================================
STEP 7 — CREATE AUTH-SERVICE PIPELINE
================================================================================

1. Go to Jenkins Dashboard → New Item
2. Name: naik-auth-deploy
3. Type: Pipeline
4. Click OK

5. In Pipeline section:
   Definition: Pipeline script from SCM
   SCM: Git
   Repository URL: https://github.com/your-username/naik-apps.git
   Credentials: github-credentials
   Branch: */main
   Script Path: naik-deploy/naik-jenkins/Jenkinsfile-auth

6. Click Save

================================================================================
STEP 8 — UPDATE JENKINSFILE WITH YOUR DETAILS
================================================================================

Edit Jenkinsfile-auth and update:

  GITHUB_REPO = 'https://github.com/YOUR-USERNAME/naik-apps.git'
  SERVICE_DIR = 'auth-service'   // path inside your repo

================================================================================
STEP 9 — RUN YOUR FIRST BUILD
================================================================================

1. Go to: naik-auth-deploy pipeline
2. Click: "Build Now"
3. Click the build number → Console Output
4. Watch the pipeline execute

Expected output:
  [Checkout]         ✅ Pulled from GitHub
  [Build JAR]        ✅ Maven built JAR
  [Build Image]      ✅ Docker image created
  [Deploy]           ✅ Container restarted
  [Health Check]     ✅ Service responding

================================================================================
STEP 10 — ADD GITHUB WEBHOOK (Phase 7 — Auto Deploy)
================================================================================

When ready for auto-deploy on push:

1. In Jenkins pipeline → Configure
   ✅ GitHub hook trigger for GITScm polling

2. In GitHub repo → Settings → Webhooks → Add webhook
   Payload URL: http://jenkins.naikorg.home.arpa/github-webhook/
   Content type: application/json
   Events: Just the push event
   ✅ Active

Now every push to main triggers the pipeline automatically.

================================================================================
FOLDER STRUCTURE ON SERVER
================================================================================

/opt/naik/
├── naik-mongodb/
│   ├── docker-compose.yml
│   └── .env
├── naik-auth/
│   ├── docker-compose.yml    ← Jenkins runs docker compose here
│   └── .env                  ← Jenkins reads this automatically
├── naik-trade-journal/
│   ├── docker-compose.yml
│   └── .env
├── naik-ui/
│   └── docker-compose.yml
└── naik-jenkins/
    └── docker-compose.yml

================================================================================
TROUBLESHOOTING
================================================================================

Problem: Docker command not found in Jenkins
Solution: Verify docker socket is mounted in docker-compose.yml
  volumes:
    - /var/run/docker.sock:/var/run/docker.sock
    - /usr/bin/docker:/usr/bin/docker

Problem: Permission denied on docker.sock
Solution: Jenkins runs as root (user: root in docker-compose.yml)

Problem: Maven not found
Solution: Configure Maven in Manage Jenkins → Tools (Step 6)

Problem: GitHub clone failed
Solution: Verify credentials ID matches Jenkinsfile exactly: github-credentials

Problem: Health check fails
Solution: Increase sleep time in Jenkinsfile (Spring Boot may need more time)
  sh 'sleep 30'  ← increase from 20 to 30

================================================================================
