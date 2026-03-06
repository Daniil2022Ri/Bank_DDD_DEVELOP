package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AuditDto;

import java.util.List;

public interface AuditService {
    List<AuditDto> findAll();
    AuditDto findById(Long id);
    List<AuditDto> findByEntityType(String entityType);
    void save(AuditDto auditDto);
}
