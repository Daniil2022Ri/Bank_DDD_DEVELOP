package com.bank.authorization.handlers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bank.authorization.dtos.errors.AuthError;
import com.bank.authorization.exceptions.CustomSignatureVerificationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthError> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(mappingError(e.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AuthError> handleEntityNotFoundException(
            EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(mappingError(e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<AuthError> handleJWTVerificationException(
            JWTVerificationException e) {
        log.error("Ошибка верификации токена", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(mappingError(e.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<AuthError> handleTokenExpiredException(TokenExpiredException e) {
        log.error("Время действия токена истекло", e);
        AuthError error = mappingError(e.getMessage(),
                HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(CustomSignatureVerificationException.class)
    public ResponseEntity<AuthError> handleCustomSignatureVerificationException(
            CustomSignatureVerificationException e) {
        log.error("Неверная подпись токена", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(mappingError(e.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    private AuthError mappingError(String message, HttpStatus httpStatus) {
        return AuthError
                .builder()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
