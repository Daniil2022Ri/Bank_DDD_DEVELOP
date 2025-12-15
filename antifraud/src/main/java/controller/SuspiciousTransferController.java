package controller;

import dto.SuspiciousAccountTransferDto;
import dto.SuspiciousCardTransferDto;
import dto.SuspiciousPhoneTransferDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import service.SuspiciousTransferService;
import java.util.List;

import static config.ApplicationConstant.CASE_CARD;
import static config.ApplicationConstant.CASE_PHONE;
import static config.ApplicationConstant.CASE_ACCOUNT;
import static config.ApplicationConstant.MSG_INVALID_TYPE;

@RestController
@RequestMapping("/suspicious-transfers")
@AllArgsConstructor
public class SuspiciousTransferController {

    private SuspiciousTransferService service;

    @Operation(
            summary = "Создать подозрительную транзакцию карты",
            description = "Создает новую запись о подозрительной транзакции по карте"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Транзакция успешно создана",
                    content = @Content(schema = @Schema(implementation = SuspiciousCardTransferDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидные данные",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PostMapping("/card")
    public ResponseEntity<SuspiciousCardTransferDto> createCard(
            @Parameter(description = "DTO для создания подозрительной транзакции карты", required = true)
            @RequestBody @Valid SuspiciousCardTransferDto dto) {
        SuspiciousCardTransferDto result = service.createCard(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(
            summary = "Создать подозрительную транзакцию телефона",
            description = "Создает новую запись о подозрительной транзакции по телефону"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Транзакция успешно создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/phone")
    public ResponseEntity<SuspiciousPhoneTransferDto> createPhone(
            @Parameter(description = "DTO для создания подозрительной транзакции телефона", required = true)
            @RequestBody @Valid SuspiciousPhoneTransferDto dto) {
        SuspiciousPhoneTransferDto result = service.createPhone(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(
            summary = "Создать подозрительную транзакцию аккаунта",
            description = "Создает новую запись о подозрительной транзакции по аккаунту"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Транзакция успешно создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/account")
    public ResponseEntity<SuspiciousAccountTransferDto> createAccount(
            @Parameter(description = "DTO для создания подозрительной транзакции аккаунта", required = true)
            @RequestBody @Valid SuspiciousAccountTransferDto dto) {
        SuspiciousAccountTransferDto result = service.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(
            summary = "Обновить подозрительную транзакцию карты",
            description = "Обновляет данные подозрительной транзакции карты по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные")
    })
    @PutMapping("/card/{id}")
    public ResponseEntity<SuspiciousCardTransferDto> updateCard(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "DTO для обновления подозрительной транзакции карты", required = true)
            @RequestBody @Valid SuspiciousCardTransferDto dto) {
        SuspiciousCardTransferDto result = service.updateCard(id, dto);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Обновить подозрительную транзакцию телефона",
            description = "Обновляет данные подозрительной транзакции телефона по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @PutMapping("/phone/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDto> updatePhone(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "DTO для обновления подозрительной транзакции телефона", required = true)
            @RequestBody @Valid SuspiciousPhoneTransferDto dto) {
        SuspiciousPhoneTransferDto result = service.updatePhone(id, dto);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Обновить подозрительную транзакцию аккаунта",
            description = "Обновляет данные подозрительной транзакции аккаунта по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @PutMapping("/account/{id}")
    public ResponseEntity<SuspiciousAccountTransferDto> updateAccount(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "DTO для обновления подозрительной транзакции аккаунта", required = true)
            @RequestBody @Valid SuspiciousAccountTransferDto dto) {
        SuspiciousAccountTransferDto result = service.updateAccount(id, dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> update(@PathVariable String type, @PathVariable Long id, @RequestBody Object dto) {
        return switch (type.toLowerCase()) {
            case CASE_CARD -> ResponseEntity.ok(service.updateCard(id, (SuspiciousCardTransferDto) dto));
            case CASE_PHONE -> ResponseEntity.ok(service.updatePhone(id, (SuspiciousPhoneTransferDto) dto));
            case CASE_ACCOUNT -> ResponseEntity.ok(service.updateAccount(id, (SuspiciousAccountTransferDto) dto));
            default -> ResponseEntity.badRequest().body(MSG_INVALID_TYPE);
        };
    }

    @Operation(
            summary = "Удалить подозрительную транзакцию карты",
            description = "Удаляет подозрительную транзакцию карты по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Транзакция успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @DeleteMapping("/card/{id}")
    public ResponseEntity<Void> deleteCard(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id) {
        service.deleteSuspiciousTransfer(id, CASE_CARD);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить подозрительную транзакцию Телефона",
            description = "Удаляет подозрительную транзакцию Телефона по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Транзакция успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @DeleteMapping("/phone/{id}")
    public ResponseEntity<Void> deletePhone(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id) {
        service.deleteSuspiciousTransfer(id, CASE_PHONE);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить подозрительную транзакцию Аккаунта",
            description = "Удаляет подозрительную транзакцию Аккаунта по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Транзакция успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @DeleteMapping("/account/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id) {
        service.deleteSuspiciousTransfer(id, CASE_ACCOUNT);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получить все подозрительные транзакции карт",
            description = "Возвращает список всех подозрительных транзакций карт"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/card")
    public ResponseEntity<List<SuspiciousCardTransferDto>> getAllCards() {
        List<SuspiciousCardTransferDto> result = service.getAllCards();
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Получить все подозрительные транзакции Телефона",
            description = "Возвращает список всех подозрительных транзакций Телефона"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/phone")
    public ResponseEntity<List<SuspiciousPhoneTransferDto>> getAllPhones() {
        return ResponseEntity.ok(service.getAllPhones());
    }

    @Operation(
            summary = "Получить все подозрительные транзакции Аккаунта",
            description = "Возвращает список всех подозрительных транзакций Аккаунта"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/account")
    public ResponseEntity<List<SuspiciousAccountTransferDto>> getAllAccounts() {
        return ResponseEntity.ok(service.getAllAccounts());
    }

    @Operation(
            summary = "Получить подозрительную транзакцию карты по ID",
            description = "Возвращает подозрительную транзакцию карты по указанному ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция найдена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/card/{id}")
    public ResponseEntity<SuspiciousCardTransferDto> getCardById(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id) {
        SuspiciousCardTransferDto result = service.getCardById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Получить подозрительную транзакцию Телефона по ID",
            description = "Возвращает подозрительную транзакцию Телефона по указанному ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция найдена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/phone/{id}")
    public ResponseEntity<SuspiciousPhoneTransferDto> getPhoneById(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getPhoneById(id));
    }
    @Operation(
            summary = "Получить подозрительную транзакцию Аккаунта по ID",
            description = "Возвращает подозрительную транзакцию Аккаунта по указанному ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция найдена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    })
    @GetMapping("/account/{id}")
    public ResponseEntity<SuspiciousAccountTransferDto> getAccountById(
            @Parameter(description = "ID транзакции", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getAccountById(id));
    }
}

