package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.service.AtmService;
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
@RequestMapping("/atms")
@RequiredArgsConstructor
@Slf4j
@Tag(name = ApplicationConstants.API_TAG_ATM, description = ApplicationConstants.API_DESCRIPTION_ATM)
public class AtmController {

    private final AtmService atmService;

    @Timed(value = "atm.create", description = "Время выполнения операции создания банкомата")
    @PostMapping
    @Operation(summary = "Создать новый банкомат", description = "Создает новый банкомат")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Банкомат успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Банк не найден")
    })
    public ResponseEntity<AtmDto> create(
            @Parameter(description = "Данные банкомата") @Valid @RequestBody AtmDto atmDto) {
        log.info("Received request to create ATM");
        AtmDto created = atmService.create(atmDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Получить все банкоматы", description = "Возвращает список всех банкоматов")
    @ApiResponse(responseCode = "200", description = "Список банкоматов успешно получен")
    public ResponseEntity<List<AtmDto>> getAll() {
        log.info("Received request to get all ATMs");
        List<AtmDto> atms = atmService.findAll();
        return ResponseEntity.ok(atms);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить банкомат по ID", description = "Возвращает банкомат по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Банкомат найден"),
            @ApiResponse(responseCode = "404", description = "Банкомат не найден")
    })
    public ResponseEntity<AtmDto> getById(@PathVariable Long id) {
        log.info("Received request to get ATM by id: {}", id);
        AtmDto atm = atmService.findById(id);
        return ResponseEntity.ok(atm);
    }

    @GetMapping("/bank/{bankDetailsId}")
    @Operation(summary = "Получить банкоматы по ID банка", description = "Возвращает список банкоматов банка")
    @ApiResponse(responseCode = "200", description = "Список банкоматов получен")
    public ResponseEntity<List<AtmDto>> getByBankDetailsId(@PathVariable Long bankDetailsId) {
        log.info("Received request to get ATMs by bank id: {}", bankDetailsId);
        List<AtmDto> atms = atmService.findByBankDetailsId(bankDetailsId);
        return ResponseEntity.ok(atms);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить банкомат", description = "Обновляет данные существующего банкомата")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Банкомат успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Банкомат не найден")
    })
    public ResponseEntity<AtmDto> update(@PathVariable Long id, @Valid @RequestBody AtmDto atmDto) {
        log.info("Received request to update ATM with id: {}", id);
        AtmDto updated = atmService.update(id, atmDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить банкомат", description = "Удаляет банкомат по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Банкомат успешно удален"),
            @ApiResponse(responseCode = "404", description = "Банкомат не найден")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received request to delete ATM with id: {}", id);
        atmService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
