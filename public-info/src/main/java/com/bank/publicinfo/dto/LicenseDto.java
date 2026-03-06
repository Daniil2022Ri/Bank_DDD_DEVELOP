package com.bank.publicinfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для лицензии банка")
public class LicenseDto {

    private Long id;

    @NotBlank(message = "Номер лицензии не может быть пустым")
    @Size(max = 50, message = "Номер лицензии не может превышать 50 символов")
    @Schema(description = "Номер лицензии", example = "Л-123456")
    private String licenseNumber;

    @NotBlank(message = "Тип лицензии не может быть пустым")
    @Size(max = 100, message = "Тип лицензии не может превышать 100 символов")
    @Schema(description = "Тип лицензии", example = "Генеральная лицензия")
    private String licenseType;

    @NotNull(message = "Дата выдачи не может быть пустой")
    @Schema(description = "Дата выдачи")
    private LocalDateTime dateOfIssue;

    @Schema(description = "Срок действия")
    private LocalDateTime expirationDate;

    @Schema(description = "Статус лицензии", example = "Действующая")
    private String status;

    @NotNull(message = "ID банка не может быть пустым")
    @Schema(description = "ID банка", example = "1")
    private Long bankDetailsId;
}
