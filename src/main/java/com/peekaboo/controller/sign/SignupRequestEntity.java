package com.peekaboo.controller.sign;


public class SignupRequestEntity extends SigninRequestEntity {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
