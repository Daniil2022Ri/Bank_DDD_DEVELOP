package service.util;

import lombok.RequiredArgsConstructor;
import model.Audit;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuditBuilder {
    private final JsonSerializer jsonSerializer;

    public Audit buildCreateAudit(String entityType, Object newEntity, String username) {
        return Audit.builder()
                .entityType(entityType)
                .operationType("CREATE")
                .createdBy(username)
                .modifiedBy(username)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .entityJson(null)
                .newEntityJson(jsonSerializer.toJson(newEntity))
                .build();
    }

    public Audit buildUpdateAudit(String entityType, Object oldEntity,
                                  Object newEntity, String username) {
        return Audit.builder()
                .entityType(entityType)
                .operationType("UPDATE")
                .createdBy(username)
                .modifiedBy(username)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .entityJson(jsonSerializer.toJson(oldEntity))
                .newEntityJson(jsonSerializer.toJson(newEntity))
                .build();
    }

    public Audit buildDeleteAudit(String entityType, Long entityId, String username) {
        return Audit.builder()
                .entityType(entityType)
                .operationType("DELETE")
                .createdBy(username)
                .modifiedBy(username)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .entityJson(null)
                .newEntityJson(String.format("{\"entityId\": %d, \"deleted\": true}", entityId))
                .build();
    }
}
