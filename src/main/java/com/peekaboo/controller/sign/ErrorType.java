package com.peekaboo.controller.sign;

public enum ErrorType {
    AUTHENTICATION_ERROR(1), USER_EXIST(2), WRONG_LOGIN_OR_PASSWORD(3), INVALID_CONFIRM_TOKEN(4);

    private int type;

    ErrorType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
