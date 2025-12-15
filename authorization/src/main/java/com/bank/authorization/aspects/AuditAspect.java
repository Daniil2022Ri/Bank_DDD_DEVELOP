package com.bank.authorization.aspects;


import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.dtos.user.UserDtoForUpdate;
import com.bank.authorization.services.AuditService;
import com.bank.authorization.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final UserService userService;
    @PersistenceContext
    private final EntityManager entityManager;

    @AfterReturning(
            "execution(void com.bank.authorization.services.UserServiceImpl.save(com.bank.authorization.dtos.user.UserDto)) && args(user)")
    public void afterCreateNewUserAdvice(UserDto user) {
        auditService.addCreate(user);
    }

    @Around(
            value = "execution(com.bank.authorization.dtos.user.UserDto com.bank.authorization.services.UserServiceImpl.update(com.bank.authorization.dtos.user.UserDtoForUpdate, long)) && args(user,id)",
            argNames = "joinPoint,user,id")
    public Object afterUpdateAdvice(ProceedingJoinPoint joinPoint, UserDtoForUpdate user, long id) throws Throwable {
        UserDto oldUser = userService.findById(id);
        entityManager.detach(oldUser);
        UserDto updatedUser = (UserDto) joinPoint.proceed();
        try {
            auditService.addUpdate(oldUser, updatedUser);
            return updatedUser;
        } catch (Exception e) {
            log.warn("Не удалось записать аудит для обнволения пользователя с id {}", id, e);
        }
        return updatedUser;

    }
}
