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
        Audit audit = new Audit();
        audit.setEntityType(entityType);
        audit.setOperationType("CREATE");
        audit.setCreatedBy(username);
        audit.setModifiedBy(username);
        audit.setCreatedAt(LocalDateTime.now());
        audit.setModifiedAt(LocalDateTime.now());
        audit.setEntityJson(null);
        audit.setNewEntityJson(jsonSerializer.toJson(newEntity));
        return audit;
    }

    public Audit buildUpdateAudit(String entityType, Object oldEntity,
                                  Object newEntity, String username) {
        Audit audit = new Audit();
        audit.setEntityType(entityType);
        audit.setOperationType("UPDATE");
        audit.setCreatedBy(username);
        audit.setModifiedBy(username);
        audit.setCreatedAt(LocalDateTime.now());
        audit.setModifiedAt(LocalDateTime.now());
        audit.setEntityJson(jsonSerializer.toJson(oldEntity));
        audit.setNewEntityJson(jsonSerializer.toJson(newEntity));
        return audit;
    }

    public Audit buildDeleteAudit(String entityType, Long entityId, String username) {
        Audit audit = new Audit();
        audit.setEntityType(entityType);
        audit.setOperationType("DELETE");
        audit.setCreatedBy(username);
        audit.setModifiedBy(username);
        audit.setCreatedAt(LocalDateTime.now());
        audit.setModifiedAt(LocalDateTime.now());
        audit.setEntityJson(null);
        audit.setNewEntityJson(String.format("{\"entityId\": %d, \"deleted\": true}", entityId));
        return audit;
    }
}