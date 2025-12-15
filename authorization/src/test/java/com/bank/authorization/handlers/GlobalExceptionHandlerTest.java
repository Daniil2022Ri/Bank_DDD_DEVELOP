package com.bank.authorization.handlers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bank.authorization.dtos.errors.AuthError;
import com.bank.authorization.exceptions.CustomSignatureVerificationException;
import com.bank.authorization.handlers.GlobalExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.Instant;

import static com.bank.authorization.utils.TestConstant.ERROR_BAD_CREDENTIALS;
import static com.bank.authorization.utils.TestConstant.ERROR_ENTITY_NOT_FOUND;
import static com.bank.authorization.utils.TestConstant.ERROR_JWT;
import static com.bank.authorization.utils.TestConstant.ERROR_TOKEN_EXPIRED;
import static com.bank.authorization.utils.TestConstant.ERROR_VERIFICATION_ERROR;
import static com.bank.authorization.utils.TestConstant.TIME_TOKEN_EXPIRED;
import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleBadCredentials() {
        ResponseEntity<AuthError> response = exceptionHandler.handleBadCredentials(
                new BadCredentialsException(ERROR_BAD_CREDENTIALS)
        );

        assertError(response, ERROR_BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handleEntityNotFoundException() {
        ResponseEntity<AuthError> response = exceptionHandler.handleEntityNotFoundException(
                new EntityNotFoundException(ERROR_ENTITY_NOT_FOUND)
        );

        assertError(response, ERROR_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Test
    void handleJWTVerificationException() {
        ResponseEntity<AuthError> response = exceptionHandler.handleJWTVerificationException(
                new JWTVerificationException(ERROR_JWT)
        );

        assertError(response, ERROR_JWT, HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handleTokenExpiredException() {
        ResponseEntity<AuthError> response = exceptionHandler.handleTokenExpiredException(
                new TokenExpiredException(ERROR_TOKEN_EXPIRED, Instant.parse(TIME_TOKEN_EXPIRED))
        );

        assertError(response, ERROR_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handleCustomSignatureVerificationException() {
        ResponseEntity<AuthError> response = exceptionHandler.handleCustomSignatureVerificationException(
                new CustomSignatureVerificationException(ERROR_VERIFICATION_ERROR)
        );

        assertError(response, ERROR_VERIFICATION_ERROR, HttpStatus.UNAUTHORIZED);
    }

    private void assertError(ResponseEntity<AuthError> response, String message, HttpStatus status) {
        assertThat(response.getStatusCode()).isEqualTo(status);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getMessage()).isEqualTo(message);
    }

}