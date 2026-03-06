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
@Schema(description = "DTO для сертификата банка")
public class CertificateDto {

    private Long id;

    @NotBlank(message = "Номер сертификата не может быть пустым")
    @Size(max = 50, message = "Номер сертификата не может превышать 50 символов")
    @Schema(description = "Номер сертификата", example = "СЕРТ-123456")
    private String certificateNumber;

    @NotBlank(message = "Тип сертификата не может быть пустым")
    @Size(max = 100, message = "Тип сертификата не может превышать 100 символов")
    @Schema(description = "Тип сертификата", example = "SSL сертификат")
    private String certificateType;

    @NotBlank(message = "Название организации не может быть пустым")
    @Size(max = 255, message = "Название организации не может превышать 255 символов")
    @Schema(description = "Название организации", example = "Comodo CA Limited")
    private String organizationName;

    @NotNull(message = "Дата выдачи не может быть пустой")
    @Schema(description = "Дата выдачи")
    private LocalDateTime dateOfIssue;

    @Schema(description = "Срок действия")
    private LocalDateTime expirationDate;

    @Schema(description = "Статус сертификата", example = "Действующий")
    private String status;

    @NotNull(message = "ID банка не может быть пустым")
    @Schema(description = "ID банка", example = "1")
    private Long bankDetailsId;
}
