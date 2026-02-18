package org.naik.auth_service.dto;

public class ValidateTokenRequest {
    
    private String token;

    //Getters and Setters
    
    public String getToken() { 
        return token; 
    }

    public void setToken(String token) { 
        this.token = token; 
    }
}
