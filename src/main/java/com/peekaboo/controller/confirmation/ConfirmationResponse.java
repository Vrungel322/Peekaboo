package com.peekaboo.controller.confirmation;

import javax.validation.constraints.NotNull;

public class ConfirmationResponse {
    @NotNull
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
