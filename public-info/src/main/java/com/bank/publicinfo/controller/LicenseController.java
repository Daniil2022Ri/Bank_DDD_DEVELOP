package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.service.LicenseService;
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
@RequestMapping("/licenses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = ApplicationConstants.API_TAG_LICENSE, description = ApplicationConstants.API_DESCRIPTION_LICENSE)
public class LicenseController {

    private final LicenseService licenseService;

    @Timed(value = "license.create", description = "Время выполнения операции создания лицензии")
    @PostMapping
    @Operation(summary = "Создать новую лицензию", description = "Создает новую лицензию банка")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Лицензия успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Банк не найден"),
            @ApiResponse(responseCode = "409", description = "Лицензия с таким номером уже существует")
    })
    public ResponseEntity<LicenseDto> create(
            @Parameter(description = "Данные лицензии") @Valid @RequestBody LicenseDto licenseDto) {
        log.info("Received request to create license");
        LicenseDto created = licenseService.create(licenseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Получить все лицензии", description = "Возвращает список всех лицензий")
    @ApiResponse(responseCode = "200", description = "Список лицензий успешно получен")
    public ResponseEntity<List<LicenseDto>> getAll() {
        log.info("Received request to get all licenses");
        List<LicenseDto> licenses = licenseService.findAll();
        return ResponseEntity.ok(licenses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить лицензию по ID", description = "Возвращает лицензию по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Лицензия найдена"),
            @ApiResponse(responseCode = "404", description = "Лицензия не найдена")
    })
    public ResponseEntity<LicenseDto> getById(@PathVariable Long id) {
        log.info("Received request to get license by id: {}", id);
        LicenseDto license = licenseService.findById(id);
        return ResponseEntity.ok(license);
    }

    @GetMapping("/bank/{bankDetailsId}")
    @Operation(summary = "Получить лицензии по ID банка", description = "Возвращает список лицензий банка")
    @ApiResponse(responseCode = "200", description = "Список лицензий получен")
    public ResponseEntity<List<LicenseDto>> getByBankDetailsId(@PathVariable Long bankDetailsId) {
        log.info("Received request to get licenses by bank id: {}", bankDetailsId);
        List<LicenseDto> licenses = licenseService.findByBankDetailsId(bankDetailsId);
        return ResponseEntity.ok(licenses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить лицензию", description = "Обновляет данные существующей лицензии")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Лицензия успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Лицензия не найдена")
    })
    public ResponseEntity<LicenseDto> update(@PathVariable Long id, @Valid @RequestBody LicenseDto licenseDto) {
        log.info("Received request to update license with id: {}", id);
        LicenseDto updated = licenseService.update(id, licenseDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить лицензию", description = "Удаляет лицензию по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Лицензия успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Лицензия не найдена")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received request to delete license with id: {}", id);
        licenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
