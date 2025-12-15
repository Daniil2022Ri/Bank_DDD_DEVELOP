package com.bank.authorization.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit")
@Getter
@Setter
@NoArgsConstructor
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "entity_type", nullable = false)
    private String entityType;
    @Column(name = "operation_type", nullable = false)
    private String operationType;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    private LocalDateTime modifiedAt;
    @Column(name = "new_entity_json", columnDefinition = "TEXT")
    @Nullable
    private String newEntityJson;
    @Column(name = "entity_json", columnDefinition = "TEXT", nullable = false)
    private String entityJson;

    public Audit(String entityType, String operationType, String createdBy, LocalDateTime createdAt, String entityJson) {
        this.entityType = entityType;
        this.operationType = operationType;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.entityJson = entityJson;
    }

    public Audit(String entityType, String operationType, String createdBy, LocalDateTime createdAt, String newEntityJson, String entityJson) {
        this.entityType = entityType;
        this.operationType = operationType;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.newEntityJson = newEntityJson;
        this.entityJson = entityJson;
    }
}
