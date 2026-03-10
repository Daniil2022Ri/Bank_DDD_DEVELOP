package com.bank.account.audti;


import com.bank.account.dto.AccountDto;
import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import com.bank.account.mapper.AuditMapper;
import com.bank.account.repository.AuditRepository;
import com.bank.account.service.AuditServiceImpl;
import com.bank.account.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private AuditMapper auditMapper;

    @InjectMocks
    private AuditServiceImpl auditService;

    private static final Long TEST_AUDIT_ID = 1L;
    private static final Long TEST_ACCOUNT_ID = 1L;
    private static final Integer TEST_ACCOUNT_NUMBER = 123456789;
    private static final Integer TEST_BANK_DETAILS_ID = 1001;
    private static final Integer TEST_PASSPORT_ID = 5001;
    private static final Integer TEST_PROFILE_ID = 3001;
    private static final BigDecimal TEST_MONEY = new BigDecimal("1500.50");
    private static final Boolean TEST_NEGATIVE_BALANCE = false;
    private static final String TEST_CURRENT_USER = "testUser";
    private static final String TEST_ENTITY_TYPE_ACCOUNT = "ACCOUNT";
    private static final String TEST_OPERATION_TYPE_CREATE = "CREATE";
    private static final String TEST_OPERATION_TYPE_UPDATE = "UPDATE";
    private static final String TEST_CREATE_METHOD = "create";
    private static final String TEST_UPDATE_METHOD = "update";

    private AuditDto testAuditDto;
    private AuditEntity testAuditEntity;
    private AccountDto testAccountDto;

    @BeforeEach
    void setUp() {
        testAccountDto = AccountDto.builder()
                .id(TEST_ACCOUNT_ID)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();

        testAuditDto = AuditDto.builder()
                .id(TEST_AUDIT_ID)
                .entityType(TEST_ENTITY_TYPE_ACCOUNT)
                .operationType(TEST_OPERATION_TYPE_CREATE)
                .createdBy(TEST_CURRENT_USER)
                .createdAt(LocalDateTime.now())
                .entityJson(JsonUtil.toJson(testAccountDto))
                .build();

        testAuditEntity = AuditEntity.builder()
                .id(TEST_AUDIT_ID)
                .entityType(TEST_ENTITY_TYPE_ACCOUNT)
                .operationType(TEST_OPERATION_TYPE_CREATE)
                .createdBy(TEST_CURRENT_USER)
                .createdAt(LocalDateTime.now())
                .entityJson(JsonUtil.toJson(testAccountDto))
                .build();
    }

    @Test
    @DisplayName("save - успешное сохранение audit записи")
    void save_ShouldSaveAuditRecord() {
        when(auditMapper.toEntity(testAuditDto)).thenReturn(testAuditEntity);
        when(auditRepository.save(testAuditEntity)).thenReturn(testAuditEntity);
        when(auditMapper.toDto(testAuditEntity)).thenReturn(testAuditDto);

        AuditDto result = auditService.save(testAuditDto);

        assertNotNull(result);
        assertEquals(TEST_AUDIT_ID, result.getId());
        assertEquals(TEST_ENTITY_TYPE_ACCOUNT, result.getEntityType());
        assertEquals(TEST_OPERATION_TYPE_CREATE, result.getOperationType());
        assertEquals(TEST_CURRENT_USER, result.getCreatedBy());
        verify(auditRepository).save(testAuditEntity);
        verify(auditMapper).toEntity(testAuditDto);
        verify(auditMapper).toDto(testAuditEntity);
    }

    @Test
    @DisplayName("save - сохранение с UPDATE операцией")
    void save_ShouldSaveAuditRecordWithUpdateOperation() {
        AuditDto updateDto = AuditDto.builder()
                .id(TEST_AUDIT_ID)
                .entityType(TEST_ENTITY_TYPE_ACCOUNT)
                .operationType(TEST_OPERATION_TYPE_UPDATE)
                .createdBy(TEST_CURRENT_USER)
                .modifiedBy(TEST_CURRENT_USER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .entityJson(JsonUtil.toJson(testAccountDto))
                .newEntityJson(JsonUtil.toJson(testAccountDto))
                .build();

        AuditEntity updateEntity = AuditEntity.builder()
                .id(TEST_AUDIT_ID)
                .entityType(TEST_ENTITY_TYPE_ACCOUNT)
                .operationType(TEST_OPERATION_TYPE_UPDATE)
                .createdBy(TEST_CURRENT_USER)
                .modifiedBy(TEST_CURRENT_USER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .entityJson(JsonUtil.toJson(testAccountDto))
                .newEntityJson(JsonUtil.toJson(testAccountDto))
                .build();

        when(auditMapper.toEntity(updateDto)).thenReturn(updateEntity);
        when(auditRepository.save(updateEntity)).thenReturn(updateEntity);
        when(auditMapper.toDto(updateEntity)).thenReturn(updateDto);

        AuditDto result = auditService.save(updateDto);

        assertNotNull(result);
        assertEquals(TEST_OPERATION_TYPE_UPDATE, result.getOperationType());
        assertEquals(TEST_CURRENT_USER, result.getModifiedBy());
        assertNotNull(result.getNewEntityJson());
        verify(auditRepository).save(updateEntity);
    }

    @Test
    @DisplayName("buildAuditDto - создание CREATE audit DTO")
    void buildAuditDto_ShouldBuildCreateAuditDto() {
        AuditDto result = auditService.buildAuditDto(testAccountDto, TEST_CREATE_METHOD, TEST_CURRENT_USER);

        assertNotNull(result);
        assertEquals(TEST_ENTITY_TYPE_ACCOUNT, result.getEntityType());
        assertEquals(TEST_OPERATION_TYPE_CREATE, result.getOperationType());
        assertEquals(TEST_CURRENT_USER, result.getCreatedBy());
        assertNull(result.getModifiedBy());
        assertNotNull(result.getEntityJson());
        assertNull(result.getNewEntityJson());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getModifiedAt());
    }

    @Test
    @DisplayName("buildAuditDto - создание UPDATE audit DTO")
    void buildAuditDto_ShouldBuildUpdateAuditDto() {
        AuditDto result = auditService.buildAuditDto(testAccountDto, TEST_UPDATE_METHOD, TEST_CURRENT_USER);

        assertNotNull(result);
        assertEquals(TEST_ENTITY_TYPE_ACCOUNT, result.getEntityType());
        assertEquals(TEST_OPERATION_TYPE_UPDATE, result.getOperationType());
        assertEquals(TEST_CURRENT_USER, result.getCreatedBy());
        assertEquals(TEST_CURRENT_USER, result.getModifiedBy());
        assertNotNull(result.getEntityJson());
        assertNotNull(result.getNewEntityJson());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getModifiedAt());
    }

    @Test
    @DisplayName("buildAuditDto - неизвестный метод считается UPDATE")
    void buildAuditDto_ShouldTreatUnknownMethodAsUpdate() {
        AuditDto result = auditService.buildAuditDto(testAccountDto, "delete", TEST_CURRENT_USER);

        assertNotNull(result);
        assertEquals(TEST_OPERATION_TYPE_UPDATE, result.getOperationType());
        assertEquals(TEST_CURRENT_USER, result.getModifiedBy());
        assertNotNull(result.getNewEntityJson());
    }

    @Test
    @DisplayName("buildAuditDto - entityJson содержит корректные данные")
    void buildAuditDto_ShouldContainValidEntityJson() {
        AuditDto result = auditService.buildAuditDto(testAccountDto, TEST_CREATE_METHOD, TEST_CURRENT_USER);

        assertNotNull(result.getEntityJson());
        assertTrue(result.getEntityJson().contains("accountNumber"));
        assertTrue(result.getEntityJson().contains(String.valueOf(TEST_ACCOUNT_NUMBER)));
        assertTrue(result.getEntityJson().contains("money"));
    }
}