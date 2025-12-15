package com.bank.authorization.services;

import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.entities.Role;
import com.bank.authorization.entities.User;
import com.bank.authorization.mappers.UserMapper;
import com.bank.authorization.repositories.AuditRepository;
import com.bank.authorization.services.AuditServiceImpl;
import com.bank.authorization.utils.TestConstant;
import com.bank.authorization.utils.UserFabricForTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static com.bank.authorization.utils.TestConstant.AUTHORIZATION;
import static com.bank.authorization.utils.TestConstant.CREATE_EVENT;
import static com.bank.authorization.utils.TestConstant.CREDENTIALS_AUTH;
import static com.bank.authorization.utils.TestConstant.NEW_JSON;
import static com.bank.authorization.utils.TestConstant.OLD_JSON;
import static com.bank.authorization.utils.TestConstant.PRINCIPAL_AUTH;
import static com.bank.authorization.utils.TestConstant.UPDATE_EVENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuditServiceImpl auditService;


    @Test
    void addCreate_SYSTEM() throws JsonProcessingException {
        UserDto userToCreate = UserFabricForTest.buildFirstUserDto();
        String json = OLD_JSON;

        doReturn(json).when(objectMapper).writeValueAsString(any(UserDto.class));

        auditService.addCreate(userToCreate);

        verify(auditRepository).save(argThat(audit ->
                audit.getEntityType().equals(userToCreate.getClass().getSimpleName()) &&
                        audit.getOperationType().equals(CREATE_EVENT) &&
                        audit.getCreatedBy().equals(AUTHORIZATION) &&
                        audit.getNewEntityJson() == null &&
                        audit.getEntityJson().equals(json)
        ));

        verify(objectMapper).writeValueAsString(userToCreate);
    }

    @Test
    void addCreate_authUser() throws JsonProcessingException {
        UserDto userToCreate = UserFabricForTest.buildFirstUserDto();
        String json = OLD_JSON;
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                PRINCIPAL_AUTH,
                CREDENTIALS_AUTH,
                Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
        ));

        doReturn(json).when(objectMapper).writeValueAsString(any(UserDto.class));

        auditService.addCreate(userToCreate);

        verify(auditRepository).save(argThat(audit ->
                audit.getEntityType().equals(userToCreate.getClass().getSimpleName()) &&
                        audit.getOperationType().equals(CREATE_EVENT) &&
                        audit.getCreatedBy().equals(PRINCIPAL_AUTH) &&
                        audit.getNewEntityJson() == null &&
                        audit.getEntityJson().equals(json)
        ));

        verify(objectMapper).writeValueAsString(userToCreate);

        SecurityContextHolder.clearContext();
    }

    @Test
    void addUpdate_authUser() throws JsonProcessingException {
        UserDto existUser = UserFabricForTest.buildFirstUserDto();
        UserDto userToUpdate = UserFabricForTest.buildExperedUpdatedUserDto();
        String newJson = NEW_JSON;
        String oldJson = OLD_JSON;
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                PRINCIPAL_AUTH,
                CREDENTIALS_AUTH,
                Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
        ));


        doReturn(oldJson).when(objectMapper).writeValueAsString(existUser);
        doReturn(newJson).when(objectMapper).writeValueAsString(userToUpdate);


        auditService.addUpdate(existUser, userToUpdate);


        verify(auditRepository).save(argThat(audit ->
                audit.getEntityType().equals(userToUpdate.getClass().getSimpleName()) &&
                        audit.getOperationType().equals(UPDATE_EVENT) &&
                        audit.getCreatedBy().equals(PRINCIPAL_AUTH) &&
                        audit.getNewEntityJson().equals(newJson) &&
                        audit.getEntityJson().equals(oldJson)
        ));
        SecurityContextHolder.clearContext();
    }

    @Test
    void addUpdate_SYSTEM() throws JsonProcessingException {
        UserDto existUser = UserFabricForTest.buildFirstUserDto();
        UserDto userToUpdate = UserFabricForTest.buildExperedUpdatedUserDto();
        String newJson = NEW_JSON;
        String oldJson = OLD_JSON;;

        doReturn(oldJson).when(objectMapper).writeValueAsString(existUser);
        doReturn(newJson).when(objectMapper).writeValueAsString(userToUpdate);


        auditService.addUpdate(existUser, userToUpdate);


        verify(auditRepository).save(argThat(audit ->
                audit.getEntityType().equals(userToUpdate.getClass().getSimpleName()) &&
                        audit.getOperationType().equals(UPDATE_EVENT) &&
                        audit.getCreatedBy().equals(AUTHORIZATION) &&
                        audit.getNewEntityJson().equals(newJson) &&
                        audit.getEntityJson().equals(oldJson)
        ));
    }


}