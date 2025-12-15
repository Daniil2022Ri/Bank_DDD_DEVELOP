package com.bank.account.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final String NOT_FOUND_BY_ID_FORMAT = "%s with id %d not found";
    private static final String NOT_FOUND_BY_FIELD_FORMAT = "%s with %s '%s' not found";

    public EntityNotFoundException(String entityName, Long id) {
        super(String.format(NOT_FOUND_BY_ID_FORMAT, entityName, id));
    }

    public EntityNotFoundException(String entityName, String fieldName, Object fieldValue) {
        super(String.format(NOT_FOUND_BY_FIELD_FORMAT, entityName, fieldName, fieldValue));
    }
}

