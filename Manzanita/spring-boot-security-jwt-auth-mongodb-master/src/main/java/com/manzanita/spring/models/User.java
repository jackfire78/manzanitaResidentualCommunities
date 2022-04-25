package com.manzanita.spring.models;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//relates back to database users table
@Document(collection = "users")
public class User {
  @Id
  private String id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(min = 6, max = 50)
  private String password;
  
  @NotBlank
  @Size(min = 10, max = 10)
  private String phoneNumber;
  
  @NotBlank
  @Size(min = 1, max = 5)
  private String unitNumber;
  
  @NotBlank
  @Size(max = 50)
  private String role;
  
  @DBRef
  private Set<Role> roles = new HashSet<>();

  public User() {
  }
  

  public User( String username, String password, String email, String phoneNumber, String unitNumber, String role) {
	this.username = username;
    this.email = email;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.unitNumber = unitNumber;
    this.role = role;
  }


  public String getId() {
    return id;
  }

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
	
  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}
