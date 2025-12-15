package exeptionsHanling;

import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static config.ApplicationConstant.MSG_ERROR_NOT_FOUND;
import static config.ApplicationConstant.MSG_ERROR_VALIDATION_ERROR;
import static config.ApplicationConstant.MSG_ERROR_INTERNAL_ERROR;
import static config.ApplicationConstant.MSG_ERROR_UNEXPECTED_ERROR;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(MSG_ERROR_NOT_FOUND, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(MSG_ERROR_VALIDATION_ERROR, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(MSG_ERROR_INTERNAL_ERROR, MSG_ERROR_UNEXPECTED_ERROR, LocalDateTime.now()));
    }
}

record ErrorResponse(String code, String message, LocalDateTime timestamp) {}
