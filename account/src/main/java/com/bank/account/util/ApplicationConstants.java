package com.bank.account.util;

import org.springframework.stereotype.Component;

@Component
public class ApplicationConstants {

    public static final String ENTITY_TYPE_ACCOUNT = "ACCOUNT";
    public static final String ENTITY_NAME_ACCOUNT = "Account";
    public static final String HEALTH_STATUS_KEY = "status";
    public static final String HEALTH_SERVICE_KEY = "service";

    public static final String DEFAULT_SYSTEM_USER = "system";
    public static final String SERVICE_NAME = "Account Service";
    public static final String CREATE_METHOD = "create";

    public static final String ERROR_ACCOUNT_NUMBER_NULL = "Account number cannot be null";
    public static final String ERROR_BANK_DETAILS_NULL = "Bank details ID cannot be null";
    public static final String ERROR_MONEY_NEGATIVE = "Money amount cannot be negative";
    public static final String ERROR_PASSPORT_NULL = "Passport ID cannot be null";
    public static final String ERROR_PROFILE_NULL = "Profile ID cannot be null";
    public static final String ERROR_ENTITY_TYPE_NULL = "Entity type cannot be null";
    public static final String ERROR_OPERATION_TYPE_NULL = "Operation type cannot be null";
    public static final String ERROR_CREATED_BY_NULL = "Created by cannot be null";
    public static final String ERROR_ENTITY_JSON_NULL = "Entity JSON cannot be null";

    public static final String AUDIT_LOGGED_FORMAT = "Audit logged for {} operation on account ID: {}";
    public static final String AUDIT_ERROR_FORMAT = "Error while logging audit: {}";
    public static final String AUDIT_SAVED_FORMAT = "Audit record saved successfully. ID: {}, Operation: {}";

    public static final String ERROR_NOT_FOUND = "Not Found";
    public static final String ERROR_VALIDATION_FAILED = "Validation Failed";
    public static final String ERROR_BAD_REQUEST = "Bad Request";
    public static final String ERROR_INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String ERROR_UNEXPECTED_ERROR = "An unexpected error occurred";

    public static final String HEALTH_STATUS_UP = "UP";
    public static final String ROOT_MESSAGE = "Account Service API is running";

    public static final String SWAGGER_TITLE = "Account Microservice API";
    public static final String SWAGGER_DESCRIPTION = "REST API для управления банковскими счетами";
    public static final String SWAGGER_VERSION = "1.0.0";
    public static final String SWAGGER_CONTACT_NAME = "Bank Development Team";
    public static final String SWAGGER_CONTACT_EMAIL = "dev@bank.com";

    public static final String API_TAG_ACCOUNT = "Account Management";
    public static final String API_DESCRIPTION_ACCOUNT = "API для управления банковскими счетами";

    private ApplicationConstants() {}

    public String getErrorNotFound() {return ERROR_NOT_FOUND;}
    public String getErrorValidationFailed() {return ERROR_VALIDATION_FAILED;}
    public String getErrorAccountNumberNull() {return ERROR_ACCOUNT_NUMBER_NULL;}
    public String getErrorBankDetailsNull() {return ERROR_BANK_DETAILS_NULL;}
    public String getErrorMoneyNegative() {return ERROR_MONEY_NEGATIVE;}
    public String getErrorPassportNull() {return ERROR_PASSPORT_NULL;}
    public String getErrorProfileNull() {return ERROR_PROFILE_NULL;}
    public String getHealthStatusKey() {return HEALTH_STATUS_KEY;}
    public String getHealthServiceKey() {return HEALTH_SERVICE_KEY;}
    public String getHealthStatusUp() {return HEALTH_STATUS_UP;}
    public String getServiceName() {return SERVICE_NAME;}
    public String getRootMessage() {return ROOT_MESSAGE;}
    public String getErrorBadRequest() {return ERROR_BAD_REQUEST;}
    public String getErrorInternalServerError() {return ERROR_INTERNAL_SERVER_ERROR;}
    public String getErrorUnexpectedError() {return ERROR_UNEXPECTED_ERROR;}
    public String getEntityTypeAccount() {return ENTITY_TYPE_ACCOUNT;}
    public String getCreateMethod() {return CREATE_METHOD;}
}

