package com.bank.authorization.dtos.errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
public class AuthError {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
