package com.bank.publicinfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для аудита изменений")
public class AuditDto {

    @Schema(description = "ID записи аудита")
    private Long id;

    @Schema(description = "Тип сущности", example = "BankDetails")
    private String entityType;

    @Schema(description = "Тип операции", example = "CREATE")
    private String operationType;

    @Schema(description = "Создано пользователем")
    private String createdBy;

    @Schema(description = "Изменено пользователем")
    private String modifiedBy;

    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;

    @Schema(description = "Дата изменения")
    private LocalDateTime modifiedAt;

    @Schema(description = "JSON старой сущности")
    private String entityJson;

    @Schema(description = "JSON новой сущности")
    private String newEntityJson;
}
