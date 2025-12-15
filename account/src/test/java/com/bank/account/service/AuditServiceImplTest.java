package com.bank.account.service;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AccountEntity;
import com.bank.account.entity.AuditEntity;
import com.bank.account.mapper.AuditMapper;
import com.bank.account.repository.AuditRepository;
import com.bank.account.util.ApplicationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    private static final Long TEST_AUDIT_ID = 1L;
    private static final Long TEST_ACCOUNT_ID = 1L;
    private static final Integer TEST_ACCOUNT_NUMBER = 123456789;
    private static final Integer TEST_BANK_DETAILS_ID = 1001;
    private static final Integer TEST_PASSPORT_ID = 5001;
    private static final Integer TEST_PROFILE_ID = 3001;
    private static final BigDecimal TEST_MONEY_AMOUNT = new BigDecimal("1500.50");
    private static final Boolean TEST_NEGATIVE_BALANCE = false;
    private static final String TEST_CURRENT_USER = "testUser";
    private static final String TEST_ENTITY_TYPE_ACCOUNT = "ACCOUNT";
    private static final String TEST_OPERATION_TYPE_CREATE = "CREATE";
    private static final String TEST_OPERATION_TYPE_UPDATE = "UPDATE";
    private static final String TEST_CREATE_METHOD = "create";
    private static final String TEST_UPDATE_METHOD = "update";
    private static final String TEST_OTHER_METHOD = "delete";
    private static final String TEST_ENTITY_JSON = "{\"test\": \"data\"}";

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private AuditMapper auditMapper;

    @Mock
    private ApplicationConstants constants;

    @InjectMocks
    private AuditServiceImpl auditService;

    @BeforeEach
    void setUp() {
        when(constants.getEntityTypeAccount()).thenReturn(TEST_ENTITY_TYPE_ACCOUNT);
        when(constants.getCreateMethod()).thenReturn(TEST_CREATE_METHOD);
    }

    @Test
    @DisplayName("Save audit - should save audit record successfully")
    void save_ShouldSaveAuditRecord() {
        AuditDto inputDto = createTestAuditDto();
        AuditEntity entity = createTestAuditEntity();
        AuditDto expectedDto = createTestAuditDto();

        when(auditMapper.toEntity(inputDto)).thenReturn(entity);
        when(auditRepository.save(entity)).thenReturn(entity);
        when(auditMapper.toDto(entity)).thenReturn(expectedDto);

        AuditDto result = auditService.save(inputDto);

        assertNotNull(result);
        assertEquals(inputDto.getEntityType(), result.getEntityType());
        assertEquals(inputDto.getOperationType(), result.getOperationType());
        verify(auditRepository).save(entity);
    }

    @Test
    @DisplayName("Build audit DTO - should build create audit DTO when method is create")
    void buildAuditDto_ShouldBuildCreateAuditDto_WhenMethodIsCreate() {
        AccountEntity account = createTestAccountEntity();

        AuditDto result = auditService.buildAuditDto(account, TEST_CREATE_METHOD, TEST_CURRENT_USER);

        assertNotNull(result);
        assertEquals(TEST_ENTITY_TYPE_ACCOUNT, result.getEntityType());
        assertEquals(TEST_OPERATION_TYPE_CREATE, result.getOperationType());
        assertEquals(TEST_CURRENT_USER, result.getCreatedBy());
        assertNull(result.getModifiedBy());
        assertNotNull(result.getEntityJson());
        assertNull(result.getNewEntityJson());
    }

    @Test
    @DisplayName("Build audit DTO - should build update audit DTO when method is update")
    void buildAuditDto_ShouldBuildUpdateAuditDto_WhenMethodIsUpdate() {
        AccountEntity account = createTestAccountEntity();

        AuditDto result = auditService.buildAuditDto(account, TEST_UPDATE_METHOD, TEST_CURRENT_USER);

        assertNotNull(result);
        assertEquals(TEST_ENTITY_TYPE_ACCOUNT, result.getEntityType());
        assertEquals(TEST_OPERATION_TYPE_UPDATE, result.getOperationType());
        assertEquals(TEST_CURRENT_USER, result.getCreatedBy());
        assertEquals(TEST_CURRENT_USER, result.getModifiedBy());
        assertNotNull(result.getEntityJson());
        assertNotNull(result.getNewEntityJson());
    }

    @Test
    @DisplayName("Build audit DTO - should handle different operation types correctly")
    void buildAuditDto_ShouldHandleDifferentOperationTypes() {
        AccountEntity account = createTestAccountEntity();

        AuditDto createResult = auditService.buildAuditDto(account, TEST_CREATE_METHOD, TEST_CURRENT_USER);
        assertEquals(TEST_OPERATION_TYPE_CREATE, createResult.getOperationType());
        assertNull(createResult.getNewEntityJson());

        AuditDto updateResult = auditService.buildAuditDto(account, TEST_UPDATE_METHOD, TEST_CURRENT_USER);
        assertEquals(TEST_OPERATION_TYPE_UPDATE, updateResult.getOperationType());
        assertNotNull(updateResult.getNewEntityJson());

        AuditDto defaultResult = auditService.buildAuditDto(account, TEST_OTHER_METHOD, TEST_CURRENT_USER);
        assertEquals(TEST_OPERATION_TYPE_UPDATE, defaultResult.getOperationType());
    }

    private AuditDto createTestAuditDto() {
        return AuditDto.builder()
                .id(TEST_AUDIT_ID)
                .entityType(TEST_ENTITY_TYPE_ACCOUNT)
                .operationType(TEST_OPERATION_TYPE_CREATE)
                .createdBy(TEST_CURRENT_USER)
                .createdAt(LocalDateTime.now())
                .entityJson(TEST_ENTITY_JSON)
                .build();
    }

    private AuditEntity createTestAuditEntity() {
        return AuditEntity.builder()
                .id(TEST_AUDIT_ID)
                .entityType(TEST_ENTITY_TYPE_ACCOUNT)
                .operationType(TEST_OPERATION_TYPE_CREATE)
                .createdBy(TEST_CURRENT_USER)
                .createdAt(LocalDateTime.now())
                .entityJson(TEST_ENTITY_JSON)
                .build();
    }

    private AccountEntity createTestAccountEntity() {
        return AccountEntity.builder()
                .id(TEST_ACCOUNT_ID)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY_AMOUNT)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();
    }
}

