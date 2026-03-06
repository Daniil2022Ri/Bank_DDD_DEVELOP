package com.bank.authorization.controllers;

import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.dtos.user.UserDtoForUpdate;
import com.bank.authorization.services.UserService;
import com.bank.authorization.utils.ApplicationConstants;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = ApplicationConstants.API_TAG_USER, description = ApplicationConstants.API_DESCRIPTION_USER)
public class UserController {

    private final UserService userService;

    @Timed(value = "user.create", description = "Время выполнения операции создания пользователя")
    @PostMapping
    @Operation(summary = "Создать нового пользователя",
            description = "Создает нового пользователя с указанными данными")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким профилем уже существует")
    })
    public ResponseEntity<UserDto> create(
            @Parameter(description = "Данные пользователя")
            @Valid @RequestBody UserDto userDto) {

        log.info("Received request to create user with profile: {}", userDto.getProfile());
        userService.save(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Received request to get all users");
        List<UserDto> users = userService.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID",
            description = "Возвращает пользователя по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserDto> getById(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable("id") Long id) {

        log.info("Received request to get user by ID: {}", id);
        UserDto user = userService.findById(id);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя",
            description = "Обновляет данные существующего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserDto> update(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Обновленные данные пользователя")
            @Valid @RequestBody UserDtoForUpdate userDto) {

        log.info("Received request to update user with ID: {}", id);
        UserDto updatedUser = userService.update(userDto, id);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя",
            description = "Удаляет пользователя по указанному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable("id") Long id) {

        log.info("Received request to delete user with ID: {}", id);
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
