package org.naik.auth_service.dto;

import jakarta.validation.constraints.NotBlank;

public class SignInRequest {
    @NotBlank(message="Enter username or Email.")
    private String username;

    private String phoneNumber;

    @NotBlank(message="Password is required!")
    private String password;

    //Getters and Setters

    public String getUsername(){
        return username;
    }

    public void setUserName(String username){
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
