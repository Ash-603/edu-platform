package com.eduplatform.model;

import lombok.Data;
import java.util.Date;

@Data
public abstract class User {
    private String userId;
    private String email;
    private String name;
    private String password;
    private boolean loggedIn;

    public void login() {
        this.loggedIn = true;
    }

    public void logout() {
        this.loggedIn = false;
    }
}