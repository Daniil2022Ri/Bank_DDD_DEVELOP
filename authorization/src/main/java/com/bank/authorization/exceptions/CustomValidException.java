package com.bank.authorization.exceptions;

public class CustomValidException extends RuntimeException {

    public CustomValidException(String message) {
        super(message);
    }
}
