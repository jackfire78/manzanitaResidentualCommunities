package com.manzanita.spring.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//class used for registering a new account. Ensure all fields aren't empty and are data validated
public class SignupRequest {
    @NotBlank
    private String username;

    //private Set<String> roles;

    @NotBlank
    private String password;   
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String phoneNumber;
    
    @NotBlank
    private String unitNumber;
    
    @NotBlank
    private String role;
 

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

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/*
	 * public Set<String> getRoles() { return this.roles; }
	 * 
	 * public void setRole(Set<String> roles) { this.roles = roles; }
	 */
}
