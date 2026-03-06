package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.service.CertificateService;
import com.bank.publicinfo.util.ApplicationConstants;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@Slf4j
@Tag(name = ApplicationConstants.API_TAG_CERTIFICATE, description = ApplicationConstants.API_DESCRIPTION_CERTIFICATE)
public class CertificateController {

    private final CertificateService certificateService;

    @Timed(value = "certificate.create", description = "Время выполнения операции создания сертификата")
    @PostMapping
    @Operation(summary = "Создать новый сертификат", description = "Создает новый сертификат банка")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Сертификат успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Банк не найден"),
            @ApiResponse(responseCode = "409", description = "Сертификат с таким номером уже существует")
    })
    public ResponseEntity<CertificateDto> create(
            @Parameter(description = "Данные сертификата") @Valid @RequestBody CertificateDto certificateDto) {
        log.info("Received request to create certificate");
        CertificateDto created = certificateService.create(certificateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Получить все сертификаты", description = "Возвращает список всех сертификатов")
    @ApiResponse(responseCode = "200", description = "Список сертификатов успешно получен")
    public ResponseEntity<List<CertificateDto>> getAll() {
        log.info("Received request to get all certificates");
        List<CertificateDto> certificates = certificateService.findAll();
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить сертификат по ID", description = "Возвращает сертификат по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сертификат найден"),
            @ApiResponse(responseCode = "404", description = "Сертификат не найден")
    })
    public ResponseEntity<CertificateDto> getById(@PathVariable Long id) {
        log.info("Received request to get certificate by id: {}", id);
        CertificateDto certificate = certificateService.findById(id);
        return ResponseEntity.ok(certificate);
    }

    @GetMapping("/bank/{bankDetailsId}")
    @Operation(summary = "Получить сертификаты по ID банка", description = "Возвращает список сертификатов банка")
    @ApiResponse(responseCode = "200", description = "Список сертификатов получен")
    public ResponseEntity<List<CertificateDto>> getByBankDetailsId(@PathVariable Long bankDetailsId) {
        log.info("Received request to get certificates by bank id: {}", bankDetailsId);
        List<CertificateDto> certificates = certificateService.findByBankDetailsId(bankDetailsId);
        return ResponseEntity.ok(certificates);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить сертификат", description = "Обновляет данные существующего сертификата")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сертификат успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Сертификат не найден")
    })
    public ResponseEntity<CertificateDto> update(@PathVariable Long id, 
                                                    @Valid @RequestBody CertificateDto certificateDto) {
        log.info("Received request to update certificate with id: {}", id);
        CertificateDto updated = certificateService.update(id, certificateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сертификат", description = "Удаляет сертификат по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Сертификат успешно удален"),
            @ApiResponse(responseCode = "404", description = "Сертификат не найден")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received request to delete certificate with id: {}", id);
        certificateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
