package com.bank.account.dto;

import com.bank.account.util.ApplicationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запись аудита")
public class AuditDto {

    @Schema(description = "ID записи аудита", example = "1")
    private Long id;

    @NotNull(message = ApplicationConstants.ERROR_ENTITY_TYPE_NULL)
    @Schema(description = "Тип сущности", example = "ACCOUNT", required = true)
    private String entityType;

    @NotNull(message = ApplicationConstants.ERROR_OPERATION_TYPE_NULL)
    @Schema(description = "Тип операции", example = "CREATE", required = true)
    private String operationType;

    @NotNull(message = ApplicationConstants.ERROR_CREATED_BY_NULL)
    @Schema(description = "Кто создал", example = "admin", required = true)
    private String createdBy;

    @Schema(description = "Кто изменил", example = "user123")
    private String modifiedBy;

    @Schema(description = "Когда создана запись", example = "2023-12-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Когда изменена запись", example = "2023-12-01T11:00:00")
    private LocalDateTime modifiedAt;

    @Schema(description = "JSON нового состояния сущности")
    private String newEntityJson;

    @NotNull(message = ApplicationConstants.ERROR_ENTITY_JSON_NULL)
    @Schema(description = "JSON состояния сущности", required = true)
    private String entityJson;
}

