package com.peekaboo.transformation;

/**
 * Created by Oleksii on 06.09.2016.
 */

public class EncoderException extends Exception {
    private static final long serialVersionUID = 1L;

    EncoderException() {
    }

    EncoderException(String message) {
        super(message);
    }

    EncoderException(Throwable cause) {
        super(cause);
    }

    EncoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
