package com.bank.publicinfo.aspect;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.service.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    @Pointcut("execution(* com.bank.publicinfo.service.BankDetailsService.create(..)) || " +
              "execution(* com.bank.publicinfo.service.BankDetailsService.update(..)) || " +
              "execution(* com.bank.publicinfo.service.BranchService.create(..)) || " +
              "execution(* com.bank.publicinfo.service.BranchService.update(..)) || " +
              "execution(* com.bank.publicinfo.service.AtmService.create(..)) || " +
              "execution(* com.bank.publicinfo.service.AtmService.update(..)) || " +
              "execution(* com.bank.publicinfo.service.LicenseService.create(..)) || " +
              "execution(* com.bank.publicinfo.service.LicenseService.update(..)) || " +
              "execution(* com.bank.publicinfo.service.CertificateService.create(..)) || " +
              "execution(* com.bank.publicinfo.service.CertificateService.update(..))")
    public void auditMethods() {
    }

    @AfterReturning(pointcut = "auditMethods()", returning = "result")
    public void auditCreateUpdate(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String entityType = getEntityType(joinPoint.getTarget().getClass().getSimpleName());
        String operationType = methodName.contains("create") ? "CREATE" : "UPDATE";

        String username = getCurrentUsername();
        String entityJson = convertToJson(result);

        AuditDto auditDto = AuditDto.builder()
                .entityType(entityType)
                .operationType(operationType)
                .createdBy(username)
                .entityJson(entityJson)
                .build();

        try {
            auditService.save(auditDto);
            log.info("Audit record created for {} operation on {}", operationType, entityType);
        } catch (Exception e) {
            log.error("Failed to create audit record: {}", e.getMessage());
        }
    }

    private String getEntityType(String serviceClassName) {
        return serviceClassName.replace("ServiceImpl", "");
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "anonymous";
    }

    private String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", e.getMessage());
            return "{}";
        }
    }
}
