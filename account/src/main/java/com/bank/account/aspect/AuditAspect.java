package com.bank.account.aspect;

import com.bank.account.dto.AccountDto;
import com.bank.account.dto.AuditDto;
import com.bank.account.service.AuditService;
import com.bank.account.service.CurrentUserService;
import com.bank.account.util.ApplicationConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static com.bank.account.util.ApplicationConstants.AUDIT_ERROR_FORMAT;
import static com.bank.account.util.ApplicationConstants.AUDIT_LOGGED_FORMAT;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final CurrentUserService currentUserService;
    private final ApplicationConstants constants;

    @AfterReturning(
            pointcut = "execution(* com.bank.account.service.AccountServiceImpl.create(..)) || " +
                    "execution(* com.bank.account.service.AccountServiceImpl.update(..))",
            returning = "result"
    )
    public void logAccountChanges(JoinPoint joinPoint, Object result) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String currentUser = currentUserService.getCurrentUser();

            if (result instanceof AccountDto account) {
                AuditDto auditDto = auditService.buildAuditDto(account, methodName, currentUser);
                auditService.save(auditDto);
                log.info(AUDIT_LOGGED_FORMAT, methodName.toUpperCase(), account.getId());
            } else if (result != null) {
                log.warn("Unexpected result type in audit aspect: {}", result.getClass().getName());
            }
        } catch (Exception e) {
            log.error(AUDIT_ERROR_FORMAT, e.getMessage(), e);

        }
    }
}


