package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.service.BankDetailsService;
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
@RequestMapping("/banks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = ApplicationConstants.API_TAG_BANK, description = ApplicationConstants.API_DESCRIPTION_BANK)
public class BankDetailsController {

    private final BankDetailsService bankDetailsService;

    @Timed(value = "bank.create", description = "Время выполнения операции создания банка")
    @PostMapping
    @Operation(summary = "Создать новый банк", description = "Создает новую запись о банке")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Банк успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "409", description = "Банк с таким БИК уже существует")
    })
    public ResponseEntity<BankDetailsDto> create(
            @Parameter(description = "Данные банка") @Valid @RequestBody BankDetailsDto bankDetailsDto) {
        log.info("Received request to create bank: {}", bankDetailsDto.getBankName());
        BankDetailsDto created = bankDetailsService.create(bankDetailsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Получить все банки", description = "Возвращает список всех банков")
    @ApiResponse(responseCode = "200", description = "Список банков успешно получен")
    public ResponseEntity<List<BankDetailsDto>> getAll() {
        log.info("Received request to get all banks");
        List<BankDetailsDto> banks = bankDetailsService.findAll();
        return ResponseEntity.ok(banks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить банк по ID", description = "Возвращает банк по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Банк найден"),
            @ApiResponse(responseCode = "404", description = "Банк не найден")
    })
    public ResponseEntity<BankDetailsDto> getById(
            @Parameter(description = "ID банка", example = "1") @PathVariable Long id) {
        log.info("Received request to get bank by id: {}", id);
        BankDetailsDto bank = bankDetailsService.findById(id);
        return ResponseEntity.ok(bank);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить банк", description = "Обновляет данные существующего банка")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Банк успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Банк не найден")
    })
    public ResponseEntity<BankDetailsDto> update(
            @Parameter(description = "ID банка", example = "1") @PathVariable Long id,
            @Parameter(description = "Обновленные данные банка") @Valid @RequestBody BankDetailsDto bankDetailsDto) {
        log.info("Received request to update bank with id: {}", id);
        BankDetailsDto updated = bankDetailsService.update(id, bankDetailsDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить банк", description = "Удаляет банк по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Банк успешно удален"),
            @ApiResponse(responseCode = "404", description = "Банк не найден")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID банка", example = "1") @PathVariable Long id) {
        log.info("Received request to delete bank with id: {}", id);
        bankDetailsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
