package com.peekaboo.controller.confirmation;

public class ConfirmationResponse {

    private String token;

    public ConfirmationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
