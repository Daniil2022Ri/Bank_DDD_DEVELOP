package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.service.AuditService;
import com.bank.publicinfo.util.ApplicationConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audits")
@RequiredArgsConstructor
@Slf4j
@Tag(name = ApplicationConstants.API_TAG_AUDIT, description = ApplicationConstants.API_DESCRIPTION_AUDIT)
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @Operation(summary = "Получить все записи аудита", description = "Возвращает список всех записей аудита")
    @ApiResponse(responseCode = "200", description = "Список записей аудита успешно получен")
    public ResponseEntity<List<AuditDto>> getAll() {
        log.info("Received request to get all audit records");
        List<AuditDto> audits = auditService.findAll();
        return ResponseEntity.ok(audits);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить запись аудита по ID", description = "Возвращает запись аудита по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запись аудита найдена"),
            @ApiResponse(responseCode = "404", description = "Запись аудита не найдена")
    })
    public ResponseEntity<AuditDto> getById(@PathVariable Long id) {
        log.info("Received request to get audit record by id: {}", id);
        AuditDto audit = auditService.findById(id);
        return ResponseEntity.ok(audit);
    }

    @GetMapping("/entity-type")
    @Operation(summary = "Получить записи аудита по типу сущности", 
               description = "Возвращает список записей аудита для указанного типа сущности")
    @ApiResponse(responseCode = "200", description = "Список записей аудита получен")
    public ResponseEntity<List<AuditDto>> getByEntityType(
            @RequestParam String entityType) {
        log.info("Received request to get audit records by entity type: {}", entityType);
        List<AuditDto> audits = auditService.findByEntityType(entityType);
        return ResponseEntity.ok(audits);
    }
}
