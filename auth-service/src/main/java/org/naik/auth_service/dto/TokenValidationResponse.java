package org.naik.auth_service.dto;

import java.util.Set;

public class TokenValidationResponse {

    private boolean valid;

    private String username;

    private Set<String> roles;

    //Constructor

    public TokenValidationResponse(boolean valid, String username, Set<String> roles) {
        this.valid = valid;
        this.username = username;
        this.roles = roles;
    }

    //Getters and Setters

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}
