package AOP;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import service.AuditService;

import java.lang.Exception;

public class TestableAuditAspect extends AuditAspect{

    @Autowired
    @Override
    public void setAuditService(AuditService auditService) {
        super.setAuditService(auditService);
    }
    @Override public void info(String msg, Object... args) { super.info(msg, args); }
    @Override public void warn(String msg, Object... args) { super.warn(msg, args); }
    @Override public void debug(String msg, Object... args) { super.debug(msg, args); }
    @Override public void error(String msg, Object... args) { super.error(msg, args); }

    @Override public void logCreateOperation(JoinPoint joinPoint, Object result) {
        super.logCreateOperation(joinPoint, result);
    }

    @Override public void logUpdateOperation(JoinPoint joinPoint, Object result) {
        super.logUpdateOperation(joinPoint, result);
    }

    @Override public void logDeleteOperation(JoinPoint joinPoint) {
        super.logDeleteOperation(joinPoint);
    }

    @Override public void logGetByIdOperations(JoinPoint joinPoint, Object result) {
        super.logGetByIdOperations(joinPoint, result);
    }

    @Override public void logExceptions(JoinPoint joinPoint, Exception ex) {
        super.logExceptions(joinPoint, ex);
    }

}
