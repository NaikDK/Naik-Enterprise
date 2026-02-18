package org.naik.auth_service.controller;

import org.naik.auth_service.dto.AuthResponse;
import org.naik.auth_service.dto.SignInRequest;
import org.naik.auth_service.dto.SignUpRequest;
import org.naik.auth_service.dto.TokenValidationResponse;
import org.naik.auth_service.dto.ValidateTokenRequest;
import org.naik.auth_service.service.AuthService;
import org.naik.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Auth Service is running!"));
    }
    
    /**
     * Register new user for Naik Organization
     * 
     * @param request - contains user details
     * @return
     *          201 - Created when user registration is successfull
     *          400 - Bad request when the details are not valid
     *          409 - Conflict if username/Email already exist
     */
    @GetMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        
        AuthResponse authResponse = authService.signUp(request);

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.success("User registration successfull!", authResponse));
    }
    
    /**
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/signin")
    public ResponseEntity<ApiResponse<AuthResponse>> signIn(@Valid @RequestBody SignInRequest request) throws Exception {
        
        AuthResponse authResponse = authService.signIn(request);
        
        return ResponseEntity.ok(
            ApiResponse.success("SignIn successful!", authResponse)
        );
    }


    /**
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken(
        @RequestBody ValidateTokenRequest request) {
        
        TokenValidationResponse response = authService.validateToken(request.getToken());

        if(!response.isValid()){
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid token!"));
        }

        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @SuppressWarnings("unchecked")
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateTokenFromHeader(
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.replace("Bearer ", "");
        
        TokenValidationResponse response = authService.validateToken(token);

        if(!response.isValid()){
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid token provided!"));
        }

        return ResponseEntity.ok(ApiResponse.success(response));

    }
    
}
