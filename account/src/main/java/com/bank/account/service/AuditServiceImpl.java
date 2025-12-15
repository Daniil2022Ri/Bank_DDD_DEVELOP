package com.bank.account.service;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AccountEntity;
import com.bank.account.entity.AuditEntity;
import com.bank.account.mapper.AuditMapper;
import com.bank.account.repository.AuditRepository;
import com.bank.account.util.ApplicationConstants;
import com.bank.account.util.JsonUtil;
import com.bank.account.util.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static com.bank.account.util.ApplicationConstants.CREATE_METHOD;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final ApplicationConstants constants;

    @Override
    @Transactional
    public AuditDto save(AuditDto auditDto) {
        log.info("Saving audit record for entity type: {}", auditDto.getEntityType());

        AuditEntity auditEntity = auditMapper.toEntity(auditDto);
        AuditEntity savedAudit = auditRepository.save(auditEntity);

        log.info(constants.AUDIT_SAVED_FORMAT,
                savedAudit.getId(), savedAudit.getOperationType());

        return auditMapper.toDto(savedAudit);
    }

    @Override
    public AuditDto buildAuditDto(AccountEntity account, String methodName, String currentUser) {
        OperationType operationType = CREATE_METHOD.equals(methodName) ?
                OperationType.CREATE : OperationType.UPDATE;

        return AuditDto.builder()
                .entityType(constants.ENTITY_TYPE_ACCOUNT)
                .operationType(operationType.getValue())
                .createdBy(currentUser)
                .modifiedBy(operationType == OperationType.UPDATE ? currentUser : null)
                .createdAt(LocalDateTime.now())
                .modifiedAt(operationType == OperationType.UPDATE ? LocalDateTime.now() : null)
                .entityJson(JsonUtil.toJson(account))
                .newEntityJson(operationType == OperationType.UPDATE ? JsonUtil.toJson(account) : null)
                .build();
    }
}


