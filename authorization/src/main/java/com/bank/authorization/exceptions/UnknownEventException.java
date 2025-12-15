package com.bank.authorization.exceptions;

public class UnknownEventException extends RuntimeException {
    public UnknownEventException(String message) {
        super(message);
    }
}
