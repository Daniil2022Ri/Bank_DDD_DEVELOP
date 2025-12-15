package com.bank.authorization.services;

import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.entities.Audit;
import com.bank.authorization.repositories.AuditRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.bank.authorization.utils.ApplicationConstants.AUTHORIZATION;
import static com.bank.authorization.utils.ApplicationConstants.CREATE_EVENT;
import static com.bank.authorization.utils.ApplicationConstants.ERROR_SERIALIZATION_TO_JSON;
import static com.bank.authorization.utils.ApplicationConstants.UPDATE_EVENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;



    @Transactional
    public void addCreate(UserDto user) {
        try {
            String createdBy = getCurrentUser();
            String entityJson = objectMapper.writeValueAsString(user);
            auditRepository.save(new Audit(
                    user.getClass().getSimpleName(),
                    CREATE_EVENT,
                    createdBy,
                    LocalDateTime.now(),
                    entityJson
            ));
            log.info("Аудит создания пользователя выполнен");
        } catch (JsonProcessingException e) {
            log.error(ERROR_SERIALIZATION_TO_JSON, e);
        }

    }

    @Transactional
    public void addUpdate(UserDto oldUser, UserDto newUser) {
        try {
            String createdBy = getCurrentUser();
            String oldEntityJson = objectMapper.writeValueAsString(oldUser);
            String entityJson = objectMapper.writeValueAsString(newUser);
            auditRepository.save(new Audit(
                    newUser.getClass().getSimpleName(),
                    UPDATE_EVENT,
                    createdBy,
                    LocalDateTime.now(),
                    entityJson,
                    oldEntityJson
            ));
            log.info("Аудит обновления пользователя выполнен");
        } catch (JsonProcessingException e) {
            log.error(ERROR_SERIALIZATION_TO_JSON, e);
        }

    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? AUTHORIZATION : authentication.getName();
    }

}
