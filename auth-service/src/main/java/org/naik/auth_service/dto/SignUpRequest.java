package org.naik.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUpRequest {
    
    @NotBlank(message="Username can not be empty.")
    private String username;

    @NotBlank(message="Email can not be empty.")
    @Pattern(
        regexp="^[a-zA-Z0-9_.+-]*@[a-zA-Z]*.[a-zA-Z]{2,}$",
        message="Email is not valid."
    )
    private String email;

    @NotBlank
    @Size(min=3, message="Password must be at least 3 characters long.")
    private String password;

    @Pattern(regexp="^\\d{10}$", message="Phone number must be 10 digits.")
    private String phoneNumber;

    @NotBlank(message="Name is mandatory.")
    private String name;

    //Getters and Setters
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    
}
