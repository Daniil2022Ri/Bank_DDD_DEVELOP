package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import com.bank.account.dto.AuditDto;

public interface AuditService {
    AuditDto save(AuditDto auditDto);
    AuditDto buildAuditDto(AccountDto account, String operationType, String currentUser);
}
