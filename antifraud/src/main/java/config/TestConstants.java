package config;

public class TestConstants {
    private TestConstants() {}

    public static final Long ID_VALID = 1L;
    public static final Long ID_NOT_FOUND = 999L;

    public static final boolean BLOCKED_TRUE = true;
    public static final boolean BLOCKED_FALSE = false;
    public static final boolean SUSPICIOUS_TRUE = true;
    public static final boolean SUSPICIOUS_FALSE = false;

    public static final String REASON_BLOCKED = "Test Blocked Reason";
    public static final String REASON_SUSPICIOUS = "Test Suspicious Reason";

    public static final String ASPECT_TYPE_CARD = "Card";
    public static final String ASPECT_TYPE_PHONE = "Phone";
    public static final String ASPECT_TYPE_ACCOUNT = "Account";

    public static final String ASPECT_METHOD_CREATE_CARD = "createCard";
    public static final String ASPECT_METHOD_UPDATE_CARD = "updateCard";

    public static final String GET_CONTAINS_CARD_ID = "getCardById";


    public static final String LOG_PART_BLOCKED_TRUE = ApplicationConstant.MSG_BLOCKED + "true";
    public static final String LOG_PART_SUSPICIOUS_TRUE = ApplicationConstant.MSG_SUSPICIOUS + "true";

    public static final String CARD = "card";
    public static final String CREATE_CARD = "createCard";
    public static final String GET_CARD_BY_ID = "getCardById";
    public static final String DATABASE_ERROR = "Database connection error";
    public static final String AUDIT_SERVICE = "auditService";
    public static final String FAILED_INJECT_AUDIT_SERVICE = "Failed to inject auditService";


}