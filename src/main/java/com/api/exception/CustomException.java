package com.api.exception;

public class CustomException extends RuntimeException {
    private final String message;
    private final int statusCode;

    // Método para criar CustomExceptions
    public CustomException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    // getters
    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}