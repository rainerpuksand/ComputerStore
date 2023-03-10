package com.example.computerstore.service;

import com.example.computerstore.entity.User;

public class AuthorizationService {

    private static AuthorizationService instance = null;
    private User user = null;

    public static AuthorizationService getInstance() {
        if (instance == null) {
            instance = new AuthorizationService();
        }
        return instance;
    }

    private AuthorizationService(){
    }

    public void login(User user) {
        this.user = user;
    }

    public void logout() {
        this.user = null;
    }

    public User getAuthState() {
        return this.user;
    }

    public long getUserId(){
        return this.user.getId();
    }

    public boolean isAdmin(){
        return this.user.getRole().equalsIgnoreCase("Admin");
    }

    public boolean isAuthenticated(){
        return this.user != null;
    }
}
