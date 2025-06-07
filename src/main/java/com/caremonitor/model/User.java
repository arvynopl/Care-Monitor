// File: src/main/java/com/caremonitor/model/User.java
package com.caremonitor.model;

import java.time.LocalDateTime;

public abstract class User {
    protected int id;
    protected String name;
    protected int age;
    protected String gender;
    protected String address;
    protected String contact;
    protected String email;
    protected String password;
    protected String fullName;
    protected String role;
    protected LocalDateTime lastLogin;
    
    public User() {}
    
    public User(int id, String name, int age, String gender, String address, String contact) {
        this.id = id;
        this.name = name;
        this.fullName = name; 
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.contact = contact;
    }
    
    public User(int id, String fullName, String email, String password, String role) {
        this.id = id;
        this.fullName = fullName;
        this.name = fullName; 
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name;
        this.fullName = name; 
    }
    
    public String getFullName() { return fullName != null ? fullName : name; }
    public void setFullName(String fullName) { 
        this.fullName = fullName;
        this.name = fullName; 
    }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}