package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.service.BranchService;
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
@RequestMapping("/branches")
@RequiredArgsConstructor
@Slf4j
@Tag(name = ApplicationConstants.API_TAG_BRANCH, description = ApplicationConstants.API_DESCRIPTION_BRANCH)
public class BranchController {

    private final BranchService branchService;

    @Timed(value = "branch.create", description = "Время выполнения операции создания отделения")
    @PostMapping
    @Operation(summary = "Создать новое отделение", description = "Создает новое отделение банка")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Отделение успешно создано"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Банк не найден")
    })
    public ResponseEntity<BranchDto> create(
            @Parameter(description = "Данные отделения") @Valid @RequestBody BranchDto branchDto) {
        log.info("Received request to create branch");
        BranchDto created = branchService.create(branchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Получить все отделения", description = "Возвращает список всех отделений")
    @ApiResponse(responseCode = "200", description = "Список отделений успешно получен")
    public ResponseEntity<List<BranchDto>> getAll() {
        log.info("Received request to get all branches");
        List<BranchDto> branches = branchService.findAll();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отделение по ID", description = "Возвращает отделение по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отделение найдено"),
            @ApiResponse(responseCode = "404", description = "Отделение не найдено")
    })
    public ResponseEntity<BranchDto> getById(@PathVariable Long id) {
        log.info("Received request to get branch by id: {}", id);
        BranchDto branch = branchService.findById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/bank/{bankDetailsId}")
    @Operation(summary = "Получить отделения по ID банка", description = "Возвращает список отделений банка")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список отделений получен")
    })
    public ResponseEntity<List<BranchDto>> getByBankDetailsId(
            @Parameter(description = "ID банка", example = "1") @PathVariable Long bankDetailsId) {
        log.info("Received request to get branches by bank id: {}", bankDetailsId);
        List<BranchDto> branches = branchService.findByBankDetailsId(bankDetailsId);
        return ResponseEntity.ok(branches);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отделение", description = "Обновляет данные существующего отделения")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отделение успешно обновлено"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Отделение не найдено")
    })
    public ResponseEntity<BranchDto> update(@PathVariable Long id,
                                              @Valid @RequestBody BranchDto branchDto) {
        log.info("Received request to update branch with id: {}", id);
        BranchDto updated = branchService.update(id, branchDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отделение", description = "Удаляет отделение по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Отделение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Отделение не найдено")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received request to delete branch with id: {}", id);
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
