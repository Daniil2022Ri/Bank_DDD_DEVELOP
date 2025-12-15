package AOP;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import service.AuditService;
import java.util.HashMap;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.util.Map;

import static config.ApplicationConstant.BY_ID_TARGET;
import static config.ApplicationConstant.ENTITY_NAME_GET_ID;
import static config.ApplicationConstant.ERR_CRITICAL_MSG;
import static config.ApplicationConstant.ERR_MSG_INP_DETAIL;
import static config.ApplicationConstant.EXCEPTION_NAME;
import static config.ApplicationConstant.GET_NAME_TARGET;
import static config.ApplicationConstant.MSG_BLOCKED;
import static config.ApplicationConstant.MSG_BLOCKED_REASON;
import static config.ApplicationConstant.MSG_CREATED;
import static config.ApplicationConstant.MSG_DELETED;
import static config.ApplicationConstant.MSG_ERR_ASPECT_CREATE;
import static config.ApplicationConstant.MSG_ERR_ASPECT_DELETE;
import static config.ApplicationConstant.MSG_ERR_ASPECT_GET;
import static config.ApplicationConstant.MSG_ERR_ASPECT_UPDATE;
import static config.ApplicationConstant.MSG_ERR_SERVICE;
import static config.ApplicationConstant.MSG_NOT_DATE;
import static config.ApplicationConstant.MSG_RECEIVED;
import static config.ApplicationConstant.MSG_SUSPICIOUS;
import static config.ApplicationConstant.MSG_UPDATED;
import static config.ApplicationConstant.NAME_METHOD_GET_BLOCKED_REASON;
import static config.ApplicationConstant.NAME_METHOD_GET_SUSPICIOUS_REASON;
import static config.ApplicationConstant.NAME_METHOD_IS_BLOCKED;
import static config.ApplicationConstant.NAME_METHOD_IS_SUSPICIOUS;
import static config.ApplicationConstant.NAME_RETURNING;
import static config.ApplicationConstant.POINTCUT_CREATE;
import static config.ApplicationConstant.POINTCUT_DELETE;
import static config.ApplicationConstant.POINTCUT_GET_BY_ID;
import static config.ApplicationConstant.POINTCUT_SERVICE;
import static config.ApplicationConstant.POINTCUT_UPDATE;
import static config.ApplicationConstant.RETURNING_RES;
import static config.ApplicationConstant.UNKNOWN_NUM;
import static config.ApplicationConstant.TYPE_CARD;
import static config.ApplicationConstant.TYPE_PHONE;
import static config.ApplicationConstant.TYPE_ACCOUNT;
import static config.ApplicationConstant.REPLACEMENT;
import static config.ApplicationConstant.SYSTEM;
import static config.ApplicationConstant.ENTITY_TYPE_UNKNOW;
import static config.ApplicationConstant.NUMBER_LENGTH_NULL;
import static config.ApplicationConstant.NUMBER_LENGTH_ONE;
import static config.ApplicationConstant.CONTAINS_CARD;
import static config.ApplicationConstant.CONTAINS_PHONE;
import static config.ApplicationConstant.CONTAINS_ACCOUNT;
import static config.ApplicationConstant.LOG_ERROR_OLD_ENTITY;


@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    private final ThreadLocal<Map<String, Object>> oldEntityCache =
            ThreadLocal.withInitial(HashMap::new);

    @AfterReturning(pointcut = POINTCUT_CREATE, returning = RETURNING_RES)
    public void logCreateOperation(JoinPoint joinPoint, Object result) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String entityType = extractEntityTypeFromMethod(methodName);
            String username = getCurrentUsername();

            auditService.logCreate(entityType, result, username);

            log.info(MSG_CREATED, entityType, methodName, extractSuspiciousDetails(result));
        } catch (Exception e) {
            log.error(MSG_ERR_ASPECT_CREATE, joinPoint.getSignature().getName(), e);
        }
    }

    @Before("execution(* service.SuspiciousTransferServiceImpl.update*(..)) && args(id, dto)")
    public void captureOldEntity(JoinPoint joinPoint, Long id, Object dto) {
        try {
            String entityType = extractEntityTypeFromMethod(
                    joinPoint.getSignature().getName());

            oldEntityCache.get().put(entityType, dto);

        } catch (Exception e) {
            log.error(LOG_ERROR_OLD_ENTITY, e.getMessage());
        }
    }

    @AfterReturning(pointcut = POINTCUT_UPDATE, returning = RETURNING_RES)
    public void logUpdateOperation(JoinPoint joinPoint, Object result) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String entityType = extractEntityTypeFromMethod(methodName);
            Long id = (Long) joinPoint.getArgs()[NUMBER_LENGTH_NULL];
            String username = getCurrentUsername();

            Object oldEntity = oldEntityCache.get().get(entityType);

            auditService.logUpdate(entityType, oldEntity, result, username);

            log.warn(MSG_UPDATED, entityType, id, extractSuspiciousDetails(result));

            oldEntityCache.get().remove(entityType);
        } catch (Exception e) {
            log.error(MSG_ERR_ASPECT_UPDATE, joinPoint.getSignature().getName(), e);
        }
    }

    @Before(POINTCUT_DELETE)
    public void logDeleteOperation(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Long id = (args.length > NUMBER_LENGTH_NULL) ? (Long) args[NUMBER_LENGTH_NULL] : null;
            String type = (args.length > NUMBER_LENGTH_ONE) ? (String) args[NUMBER_LENGTH_ONE] : UNKNOWN_NUM;
            String username = getCurrentUsername();
            String entityType = convertToEntityType(type);

            auditService.logDelete(entityType, id, username);

            log.info(MSG_DELETED, type, id);
        } catch (Exception e) {
            log.error(MSG_ERR_ASPECT_DELETE, joinPoint.getSignature().getName(), e);
        }
    }

    @AfterThrowing(pointcut = POINTCUT_SERVICE, throwing = EXCEPTION_NAME)
    public void logExceptions(JoinPoint joinPoint, Exception ex) {
        try {
            String methodName = joinPoint.getSignature().getName();
            log.error(MSG_ERR_SERVICE, methodName, ex.getMessage());
        } catch (Exception e) {
            log.error(ERR_CRITICAL_MSG, e);
        }
    }

    @AfterReturning(pointcut = POINTCUT_GET_BY_ID, returning = NAME_RETURNING)
    public void logGetByIdOperations(JoinPoint joinPoint, Object result) {
        try {
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();
            Long id = (args.length > NUMBER_LENGTH_NULL) ? (Long) args[NUMBER_LENGTH_NULL] : null;
            String entityType = methodName.replace(GET_NAME_TARGET, REPLACEMENT)
                    .replace(BY_ID_TARGET, REPLACEMENT);
            log.debug(MSG_RECEIVED, entityType, id);
        } catch (Exception e) {
            log.error(MSG_ERR_ASPECT_GET, joinPoint.getSignature().getName(), e);
        }
    }

    private String extractEntityTypeFromMethod(String methodName) {
        if (methodName.contains(CONTAINS_CARD)) return TYPE_CARD;
        if (methodName.contains(CONTAINS_PHONE)) return TYPE_PHONE;
        if (methodName.contains(CONTAINS_ACCOUNT)) return TYPE_ACCOUNT;
        return ENTITY_TYPE_UNKNOW;
    }

    private String convertToEntityType(String type) {
        return switch (type.toLowerCase()) {
            case TYPE_CARD -> TYPE_CARD;
            case TYPE_PHONE -> TYPE_PHONE;
            case TYPE_ACCOUNT -> TYPE_ACCOUNT;
            default -> ENTITY_TYPE_UNKNOW;
        };
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : SYSTEM;
    }

    private Long extractId(Object dto) {
        if (dto == null) return null;
        try {
            Method getIdMethod = dto.getClass().getMethod(ENTITY_NAME_GET_ID);
            return (Long) getIdMethod.invoke(dto);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractSuspiciousDetails(Object dto) {
        if (dto == null) return MSG_NOT_DATE;
        try {
            Class<?> dtoClass = dto.getClass();
            Boolean isBlocked = (Boolean) invokeSafe(dtoClass, dto, NAME_METHOD_IS_BLOCKED);
            Boolean isSuspicious = (Boolean) invokeSafe(dtoClass, dto, NAME_METHOD_IS_SUSPICIOUS);
            String blockedReason = (String) invokeSafe(dtoClass, dto, NAME_METHOD_GET_BLOCKED_REASON);
            String suspiciousReason = (String) invokeSafe(dtoClass, dto, NAME_METHOD_GET_SUSPICIOUS_REASON);

            return String.format("%s%s, %s%s, %s%s, %s%s",
                    MSG_BLOCKED, isBlocked,
                    MSG_SUSPICIOUS, isSuspicious,
                    MSG_BLOCKED_REASON, blockedReason,
                    MSG_SUSPICIOUS + MSG_BLOCKED_REASON, suspiciousReason);
        } catch (Exception e) {
            return ERR_MSG_INP_DETAIL + e.getMessage();
        }
    }

    private Object invokeSafe(Class<?> clazz, Object obj, String methodName) {
        try {
            return clazz.getMethod(methodName).invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    protected void info(String msg, Object... args) { log.info(msg, args); }
    protected void warn(String msg, Object... args) { log.warn(msg, args); }
    protected void debug(String msg, Object... args) { log.debug(msg, args); }
    protected void error(String msg, Object... args) { log.error(msg, args); }

}