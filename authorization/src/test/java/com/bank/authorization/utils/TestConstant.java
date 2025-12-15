package com.bank.authorization.utils;

import com.google.inject.spi.StaticInjectionRequest;

public final class TestConstant {
    public static final long FIRST_USER_PROFILE_ID = 111;
    public static final String OLD_PASSWORD = "oldPassword";
    public static final long ID_FOR_DELETE = 1;
    public static final String ENCODE_NEW_PASSWORD = "encodeNewPassword";
    public static final long SECOND_USER_PROFILE_ID = 222;
    public static final long NOT_EXISTS_PROFILE = 4234243;
    public static final long ID_FOR_UPDATE = 1;
    public static final String NEW_PASSWORD = "newPassword";
    public static final String STATUS_OK = "OK";
    public static final long SEARCH_ID = 1;
    public static final long SECOND_SEARCH_ID = 2;
    public static final long ID_FIRST_USER_FROM_DB = 1;
    public static final long ID_SECOND_USER_FROM_DB = 2;
    public static final String ERROR_ENTITY_NOT_FOUND = "Пользователь с id 1 не найден";
    public static final long UPDATED_PROFILE_ID = 333;
    public static final String ERROR_EXISTS_PROFILE_ID = "Такой Profile id существует";
    public static final String AUTHORIZATION = "AUTHORIZATION";
    public static final String CREATE_EVENT = "CREATE";
    public static final String UPDATE_EVENT = "UPDATE";
    public static final String OLD_JSON = """
                {
                    "profile": 111,
                    "password": "oldPassword",
                    "role": "ROLE_USER"
                }
                """;
    public static final String NEW_JSON = """
                {
                    "profile": 333,
                    "password": "newPassword",
                    "role": "ROLE_ADMIN"
                }
                """;
    public static final String PRINCIPAL_AUTH = "testUser";
    public static final String CREDENTIALS_AUTH = "testPassword";
    public static final String DATABASE_NAME = "testdb";
    public static final String DATABASE_PASSWORD = "test";
    public static final String DATABASE_USERNAME = "test";

    public static final String NAME_TOPIC = "test-topic";
    public static final int PARTITION = 0;
    public static final long OFFSET = 0;
    public static final String MESSAGE_KEY = "key";
    public static final String MESSAGE_VALUE = "test-value";
    public static final String ERROR_HANDLER = "error";
    public static final String VALIDATION_ERROR = "Validation error";
    public static final String ERROR_UNKNOWN_EVENT = "unknown error";
    public static final String ERROR_TIMEOUT = "timeout error";
    public static final String ERROR_NETWORK = "network error";
    public static final String ERROR_JWT = "jwt error";
    public static final String ERROR_TOKEN_EXPIRED = "token expired";
    public static final String TIME_TOKEN_EXPIRED = "2025-11-18T10:30:00Z";
    public static final String ERROR_VERIFICATION_ERROR = "verification error";



    public static final String LOGIN_USERNAME = "username";
    public static final String LOGIN_PASSWORD = "password";
    public static final String ENDPOINT_AUTH = "/auth";
    public static final String ERROR_BAD_CREDENTIALS = "Логин или пароль не верный";
    public static final String HEADER_VALID_TOKEN = "bearer: validToken";
    public static final String JSON_PATH_TOKEN = "$.token";
    public static final String JSON_PATH_MESSAGE = "$.message";






}
