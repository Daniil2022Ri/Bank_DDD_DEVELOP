package com.bank.authorization.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class CustomSignatureVerificationException extends JWTVerificationException {

    public CustomSignatureVerificationException(String message) {
        super(message);
    }
}
