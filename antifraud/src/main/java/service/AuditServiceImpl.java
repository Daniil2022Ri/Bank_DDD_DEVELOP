package service;

import AOP.TestableAuditAspect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.AuditDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Audit;
import org.springframework.stereotype.Service;
import repository.AuditRepository;
import service.util.AuditBuilder;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final TestableAuditAspect testableAuditAspect;
    private final AuditRepository auditRepository;
    private final AuditBuilder auditBuilder;

    @Override
    public void logCreate(String entityType, Object newEntity, String username) {
        try {
            Audit audit = auditBuilder.buildCreateAudit(entityType, newEntity, username);
            auditRepository.save(audit);
            testableAuditAspect.info("Аудит создания сохранен: {}", entityType);
        } catch (Exception e) {
            testableAuditAspect.error("Ошибка при сохранении аудита создания: {}", e.getMessage());
        }
    }

    @Override
    public void logUpdate(String entityType, Object oldEntity, Object newEntity, String username) {
        try {
            Audit audit = auditBuilder.buildUpdateAudit(entityType, oldEntity, newEntity, username);
            auditRepository.save(audit);
            testableAuditAspect.info("Аудит обновления сохранен: {}", entityType);
        } catch (Exception e) {
            testableAuditAspect.error("Ошибка при сохранении аудита обновления: {}", e.getMessage());
        }
    }

    @Override
    public void logDelete(String entityType, Long entityId, String username) {
        try {
            Audit audit = auditBuilder.buildDeleteAudit(entityType, entityId, username);
            auditRepository.save(audit);
            testableAuditAspect.info("Аудит удаления сохранен: {} ID: {}", entityType, entityId);
        } catch (Exception e) {
            testableAuditAspect.error("Ошибка при сохранении аудита удаления: {}", e.getMessage());
        }
    }
}