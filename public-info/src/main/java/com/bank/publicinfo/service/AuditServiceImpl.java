package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.mapper.AuditMapper;
import com.bank.publicinfo.model.AuditEntity;
import com.bank.publicinfo.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;

    @Override
    public List<AuditDto> findAll() {
        log.info("Fetching all audit records");
        return auditRepository.findAll().stream()
                .map(auditMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuditDto findById(Long id) {
        log.info("Fetching audit record by id: {}", id);
        AuditEntity entity = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit record not found with id: " + id));
        return auditMapper.toDto(entity);
    }

    @Override
    public List<AuditDto> findByEntityType(String entityType) {
        log.info("Fetching audit records by entity type: {}", entityType);
        return auditRepository.findByEntityType(entityType).stream()
                .map(auditMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(AuditDto auditDto) {
        log.info("Saving audit record");
        AuditEntity entity = auditMapper.toEntity(auditDto);
        auditRepository.save(entity);
        log.info("Audit record saved");
    }
}
