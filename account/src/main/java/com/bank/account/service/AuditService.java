package com.bank.account.service;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AccountEntity;

public interface AuditService {
    AuditDto save(AuditDto auditDto);
    AuditDto buildAuditDto(AccountEntity account, String operationType, String currentUser);
}
