package com.bank.account.controller;

import com.bank.account.dto.AccountDto;
import com.bank.account.service.AccountService;
import com.bank.account.util.ApplicationConstants;
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
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = ApplicationConstants.API_TAG_ACCOUNT, description = ApplicationConstants.API_DESCRIPTION_ACCOUNT)
public class AccountController {

    private final AccountService accountService;

    @Timed(value = "account.create", description = "Время выполнения операции создания аккаунта")
    @PostMapping
    @Operation(summary = "Создать новую учетную запись",
            description = "Создает новую банковскую учетную запись с указанными данными")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Учетная запись успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "409", description = "Учетная запись с таким номером уже существует")
    })
    public ResponseEntity<AccountDto> create(
            @Parameter(description = "Данные учетной записи")
            @Valid @RequestBody AccountDto accountDto) {

        log.info("Received request to create account with number: {}", accountDto.getAccountNumber());
        AccountDto createdAccount = accountService.create(accountDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить учетную запись по ID",
            description = "Возвращает учетную запись по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учетная запись найдена"),
            @ApiResponse(responseCode = "404", description = "Учетная запись не найдена")
    })
    public ResponseEntity<AccountDto> getById(
            @Parameter(description = "ID учетной записи", example = "1")
            @PathVariable("id") Long id) {

        log.info("Received request to get account by ID: {}", id);
        AccountDto account = accountService.findById(id);

        return ResponseEntity.ok(account);
    }

    @GetMapping("/by-account-number/{accountNumber}")
    @Operation(summary = "Получить учетную запись по номеру счета",
            description = "Возвращает учетную запись по указанному номеру счета")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учетная запись найдена"),
            @ApiResponse(responseCode = "404", description = "Учетная запись не найдена")
    })
    public ResponseEntity<AccountDto> getByAccountNumber(
            @Parameter(description = "Номер счета", example = "123456789")
            @PathVariable("accountNumber") Integer accountNumber) {

        log.info("Received request to get account by number: {}", accountNumber);
        AccountDto account = accountService.findByAccountNumber(accountNumber);

        return ResponseEntity.ok(account);
    }

    @GetMapping("/by-profile/{profileId}")
    @Operation(summary = "Получить учетную запись по ID профиля",
            description = "Возвращает учетную запись по указанному ID профиля")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учетная запись найдена"),
            @ApiResponse(responseCode = "404", description = "Учетная запись не найдена")
    })
    public ResponseEntity<AccountDto> getByProfileId(
            @Parameter(description = "ID профиля", example = "3001")
            @PathVariable("profileId") Integer profileId) {

        log.info("Received request to get account by profile ID: {}", profileId);
        AccountDto account = accountService.findByProfileId(profileId);

        return ResponseEntity.ok(account);
    }

    @GetMapping
    @Operation(summary = "Получить все учетные записи",
            description = "Возвращает список всех банковских учетных записей")
    @ApiResponse(responseCode = "200", description = "Список учетных записей успешно получен")
    public ResponseEntity<List<AccountDto>> getAll() {
        log.info("Received request to get all accounts");
        List<AccountDto> accounts = accountService.findAll();

        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить учетную запись",
            description = "Обновляет данные существующей учетной записи")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учетная запись успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Учетная запись не найдена"),
            @ApiResponse(responseCode = "409", description = "Конфликт данных (номер счета уже существует)")
    })
    public ResponseEntity<AccountDto> update(
            @Parameter(description = "ID учетной записи", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Обновленные данные учетной записи")
            @Valid @RequestBody AccountDto accountDto) {

        log.info("Received request to update account with ID: {}", id);
        AccountDto updatedAccount = accountService.update(id, accountDto);

        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить учетную запись",
            description = "Удаляет учетную запись по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Учетная запись успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Учетная запись не найдена")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID учетной записи", example = "1")
            @PathVariable("id") Long id) {

        log.info("Received request to delete account with ID: {}", id);
        accountService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

