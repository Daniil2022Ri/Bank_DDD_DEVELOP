package com.bank.authorization.utils;

public final class ApplicationConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";
    public static final String ENDPOINT_AUTH = "/auth";
    public static final String ENDPOINT_ACTUATOR_ALL = "/actuator/**";
    public static final int MAX_RETRY = 3;
    public static final String ERROR_KAFKA_HANDLER = "Ошибка при обработке сообщения из топика {}:{}";
    public static final String PATTERN_FOR_FIELD_ERROR = "%s: %s";
    public static final String FOR_JOINING = ", ";
    public static final String EXISTS_PROFILE_ID = "Такой Profile id существует";
    public static final String CLAIM_ROLE = "role";
    public static final String ERROR_SERIALIZATION_TO_JSON = "Ошибка сериализации пользователя в аудит";
    public static final String AUTHORIZATION = "AUTHORIZATION";
    public static final String CREATE_EVENT = "CREATE";
    public static final String UPDATE_EVENT = "UPDATE";

}
