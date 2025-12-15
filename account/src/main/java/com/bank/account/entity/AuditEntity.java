package com.bank.account.entity;

import com.bank.account.util.ApplicationConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit", schema = "account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", length = 40, nullable = false)
    @NotNull(message = ApplicationConstants.ERROR_ENTITY_TYPE_NULL)
    private String entityType;

    @Column(name = "operation_type", length = 255, nullable = false)
    @NotNull(message = ApplicationConstants.ERROR_OPERATION_TYPE_NULL)
    private String operationType;

    @Column(name = "created_by", length = 255, nullable = false)
    @NotNull(message = ApplicationConstants.ERROR_CREATED_BY_NULL)
    private String createdBy;

    @Column(name = "modified_by", length = 255)
    private String modifiedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "new_entity_json", columnDefinition = "TEXT")
    private String newEntityJson;

    @Column(name = "entity_json", columnDefinition = "TEXT", nullable = false)
    @NotNull(message = ApplicationConstants.ERROR_ENTITY_JSON_NULL)
    private String entityJson;
}
