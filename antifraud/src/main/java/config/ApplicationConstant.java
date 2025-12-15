package config;

public class ApplicationConstant {

    public static final String OPERATION_UPDATE = "UPDATE";
    public static final String OPERATION_CREATE = "CREATE";
    public static final String OPERATION_DELETE = "DELETE";

    public static final String TYPE_CARD = "card";
    public static final String TYPE_PHONE = "phone";
    public static final String TYPE_ACCOUNT = "account";

    public static final String MSG_CREATED = "СОЗДАНА подозрительная транзакция: Тип={}, Метод={}, Данные={}";
    public static final String MSG_UPDATED = "ОБНОВЛЕНА подозрительная транзакция: Тип={}, ID={}, Данные={}";
    public static final String MSG_DELETED = "УДАЛЕНИЕ подозрительной транзакции: Тип={}, ID={}";
    public static final String MSG_RECEIVED = "Получена подозрительная транзакция: Тип={}, ID={}";
    public static final String MSG_ERR_SERVICE = "ОШИБКА в сервисе подозрительных транзакций: Метод={}, Ошибка={}";
    public static final String MSG_ERR_ASPECT_CREATE = "Ошибка в аспекте при логировании создания: {}";
    public static final String MSG_ERR_ASPECT_UPDATE = "Ошибка в аспекте при логировании обновления: {}";
    public static final String MSG_ERR_ASPECT_DELETE = "Ошибка в аспекте при логировании удаления: {}";
    public static final String MSG_ERR_ASPECT_GET = "Ошибка в аспекте при логировании получения по ID: {}";

    public static final String ERR_NOT_FOUND_TEMPLATE = "%s не найден с ID: %s";
    public static final String ERR_CARD_NOT_FOUND = "Карта";
    public static final String ERR_PHONE_NOT_FOUND = "Телефон";
    public static final String ERR_ACCOUNT_NOT_FOUND = "Аккаунт";
    public static final String ERR_INVALID_TYPE = "Недопустимый тип транзакции: ";
    public static final String ERR_MSG_INP_DETAIL = "Ошибка извлечения деталей: ";


    public static final String POINTCUT_SERVICE = "execution(* service.SuspiciousTransferServiceImpl.*(..))";
    public static final String POINTCUT_CREATE = "execution(* service.SuspiciousTransferServiceImpl.create*(..))";
    public static final String POINTCUT_UPDATE = "execution(* service.SuspiciousTransferServiceImpl.update*(..))";
    public static final String POINTCUT_DELETE = "execution(* service.SuspiciousTransferServiceImpl.deleteSuspiciousTransfer(..))";
    public static final String POINTCUT_GET_BY_ID = "execution(* service.SuspiciousTransferServiceImpl.get*ById(..))";

    public static final String MSG_NOT_DATE = "Нет данных";


    public static final String ERR_CRITICAL_MSG = "Критическая ошибка в аспекте логирования исключений";


    public static final String NAME_METHOD_IS_BLOCKED = "isBlocked";
    public static final String NAME_METHOD_IS_SUSPICIOUS = "isSuspicious";
    public static final String NAME_METHOD_GET_BLOCKED_REASON = "getBlockedReason";
    public static final String NAME_METHOD_GET_SUSPICIOUS_REASON = "getSuspiciousReason";
    public static final String NAME_RETURNING = "result";
    public static final String BY_ID_TARGET = "ById";
    public static final String GET_NAME_TARGET = "get";
    public static final String ENTITY_NAME_GET_ID = "getId";
    public static final String EXCEPTION_NAME = "ex";
    public static final String UNKNOWN_NUM= "Unknown";

    public static final String RETURNING_RES = "result";
    public static final String METHOD_TARGET_CREATE = "create";
    public static final String METHOD_TARGET_UPDATE = "update";

    public static final String MSG_BLOCKED = "Заблокирована=";
    public static final String MSG_SUSPICIOUS = "Подозрительна= ";
    public static final String MSG_BLOCKED_REASON = "Причина= ";

    public static final String CASE_CARD = "card";
    public static final String CASE_PHONE = "phone";
    public static final String CASE_ACCOUNT = "account";

    public static final String MSG_INVALID_TYPE = "Invalid type";

    public static final String MSG_ERROR_NOT_FOUND = "NOT_FOUND";
    public static final String MSG_ERROR_VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String MSG_ERROR_INTERNAL_ERROR = "INTERNAL_ERROR";

    public static final String MSG_ERROR_UNEXPECTED_ERROR = "Unexpected error";
    public static final String ENTITY_TYPE_UNKNOW = "entity type unknow";

    public static final String AUDIT_SERIALIZATION_ERROR = "Error Serialization Audit";
    public static final String REPLACEMENT = "";
    public static final String SYSTEM = "system";

    public static final int NUMBER_LENGTH_NULL = 0;
    public static final int NUMBER_LENGTH_ONE = 1;

    public static final String CONTAINS_CARD = "Card";
    public static final String CONTAINS_PHONE = "Phone";
    public static final String CONTAINS_ACCOUNT = "Account";

    public static final String LOG_ERROR_OLD_ENTITY = "Ошибка при захвате старой сущности: {}";

}
