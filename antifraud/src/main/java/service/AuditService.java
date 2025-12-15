package service;

import dto.AuditDto;

public interface AuditService {

    void logCreate(String entityType, Object newEntity, String username);
    void logUpdate(String entityType, Object oldEntity, Object newEntity, String username);
    void logDelete(String entityType, Long entityId, String username);

}
