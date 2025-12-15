package com.bank.account.exception;

import com.bank.account.util.ApplicationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String TEST_ENTITY_NAME = "Account";
    private static final Long TEST_ENTITY_ID = 1L;
    private static final String TEST_FIELD_NAME = "accountNumber";
    private static final Object TEST_FIELD_VALUE = "123456";
    private static final String TEST_ERROR_MESSAGE = "Test error message";
    private static final String TEST_VALIDATION_ERROR_MESSAGE = "Input validation failed";
    private static final String TEST_ACCOUNT_NUMBER_ERROR = "Account number cannot be null";
    private static final String TEST_MONEY_ERROR = "Money cannot be negative";

    private static final String TEST_ERROR_NOT_FOUND = "Not Found";
    private static final String TEST_ERROR_VALIDATION_FAILED = "Validation Failed";
    private static final String TEST_ERROR_BAD_REQUEST = "Bad Request";
    private static final String TEST_ERROR_INTERNAL_SERVER_ERROR = "Internal Server Error";
    private static final String TEST_ERROR_UNEXPECTED_ERROR = "An unexpected error occurred";

    private static final int TEST_STATUS_NOT_FOUND = 404;
    private static final int TEST_STATUS_BAD_REQUEST = 400;
    private static final int TEST_STATUS_INTERNAL_SERVER_ERROR = 500;

    private static final String TEST_NOT_FOUND_BY_ID_FORMAT = "%s with id %d not found";
    private static final String TEST_NOT_FOUND_BY_FIELD_FORMAT = "%s with %s '%s' not found";

    @Mock
    private ApplicationConstants constants;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        when(constants.getErrorNotFound()).thenReturn(TEST_ERROR_NOT_FOUND);
        when(constants.getErrorValidationFailed()).thenReturn(TEST_ERROR_VALIDATION_FAILED);
        when(constants.getErrorBadRequest()).thenReturn(TEST_ERROR_BAD_REQUEST);
        when(constants.getErrorInternalServerError()).thenReturn(TEST_ERROR_INTERNAL_SERVER_ERROR);
        when(constants.getErrorUnexpectedError()).thenReturn(TEST_ERROR_UNEXPECTED_ERROR);
    }

    @Test
    @DisplayName("Handle EntityNotFoundException - returns 404 status")
    void handleEntityNotFound_ShouldReturnNotFoundResponse() {
        EntityNotFoundException exception = new EntityNotFoundException(TEST_ENTITY_NAME, TEST_ENTITY_ID);
        String expectedMessage = String.format(TEST_NOT_FOUND_BY_ID_FORMAT, TEST_ENTITY_NAME, TEST_ENTITY_ID);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleEntityNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_STATUS_NOT_FOUND, response.getBody().getStatus());
        assertEquals(TEST_ERROR_NOT_FOUND, response.getBody().getError());
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Handle EntityNotFoundException with field - returns 404 status")
    void handleEntityNotFoundWithField_ShouldReturnNotFoundResponse() {
        EntityNotFoundException exception = new EntityNotFoundException(TEST_ENTITY_NAME, TEST_FIELD_NAME, TEST_FIELD_VALUE);
        String expectedMessage = String.format(TEST_NOT_FOUND_BY_FIELD_FORMAT, TEST_ENTITY_NAME, TEST_FIELD_NAME, TEST_FIELD_VALUE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleEntityNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_STATUS_NOT_FOUND, response.getBody().getStatus());
        assertEquals(TEST_ERROR_NOT_FOUND, response.getBody().getError());
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Handle MethodArgumentNotValidException - returns 400 status with validation errors")
    void handleValidationExceptions_ShouldReturnBadRequestWithErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError(TEST_ENTITY_NAME, TEST_FIELD_NAME, TEST_ERROR_MESSAGE);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<ValidationErrorResponse> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_STATUS_BAD_REQUEST, response.getBody().getStatus());
        assertEquals(TEST_ERROR_VALIDATION_FAILED, response.getBody().getError());
        assertEquals(TEST_VALIDATION_ERROR_MESSAGE, response.getBody().getMessage());

        Map<String, String> errors = response.getBody().getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(TEST_ERROR_MESSAGE, errors.get(TEST_FIELD_NAME));
    }

    @Test
    @DisplayName("Handle MethodArgumentNotValidException with multiple errors - returns all errors")
    void handleValidationExceptionsWithMultipleErrors_ShouldReturnAllErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError(TEST_ENTITY_NAME, "accountNumber", TEST_ACCOUNT_NUMBER_ERROR);
        FieldError fieldError2 = new FieldError(TEST_ENTITY_NAME, "money", TEST_MONEY_ERROR);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<ValidationErrorResponse> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, String> errors = response.getBody().getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals(TEST_ACCOUNT_NUMBER_ERROR, errors.get("accountNumber"));
        assertEquals(TEST_MONEY_ERROR, errors.get("money"));
    }

    @Test
    @DisplayName("Handle IllegalArgumentException - returns 400 status")
    void handleIllegalArgument_ShouldReturnBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException(TEST_ERROR_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgument(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_STATUS_BAD_REQUEST, response.getBody().getStatus());
        assertEquals(TEST_ERROR_BAD_REQUEST, response.getBody().getError());
        assertEquals(TEST_ERROR_MESSAGE, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Handle generic Exception - returns 500 status")
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception exception = new Exception(TEST_ERROR_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_STATUS_INTERNAL_SERVER_ERROR, response.getBody().getStatus());
        assertEquals(TEST_ERROR_INTERNAL_SERVER_ERROR, response.getBody().getError());
        assertEquals(TEST_ERROR_UNEXPECTED_ERROR, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Handle RuntimeException - returns 500 status")
    void handleRuntimeException_ShouldReturnInternalServerError() {
        RuntimeException exception = new RuntimeException(TEST_ERROR_MESSAGE);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_STATUS_INTERNAL_SERVER_ERROR, response.getBody().getStatus());
        assertEquals(TEST_ERROR_INTERNAL_SERVER_ERROR, response.getBody().getError());
        assertEquals(TEST_ERROR_UNEXPECTED_ERROR, response.getBody().getMessage());
    }
}

