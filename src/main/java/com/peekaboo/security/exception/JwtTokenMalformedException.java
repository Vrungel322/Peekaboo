package com.peekaboo.security.exception;

public class JwtTokenMalformedException extends RuntimeException {

    public JwtTokenMalformedException(String message) {
        super(message);
    }
}
