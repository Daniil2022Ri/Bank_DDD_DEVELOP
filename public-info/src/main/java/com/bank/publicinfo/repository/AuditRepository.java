package com.bank.publicinfo.repository;

import com.bank.publicinfo.model.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, Long> {
    List<AuditEntity> findByEntityType(String entityType);
    List<AuditEntity> findByCreatedBy(String createdBy);
}
