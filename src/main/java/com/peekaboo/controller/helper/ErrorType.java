package com.peekaboo.controller.helper;

public enum  ErrorType {
    AUTHENTICATION_ERROR(1), USER_EXIST(2), WRONG_LOGIN_OR_PASSWORD(3);

    private int type;

    ErrorType(int type) {
        this.type = type;
    }
}
