package com.peekaboo.controller.utils;

public class ErrorResponse {

    private ErrorType type;
    private String message;

    public ErrorResponse(ErrorType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ErrorType getType() {
        return type;
    }

    public void setType(ErrorType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
