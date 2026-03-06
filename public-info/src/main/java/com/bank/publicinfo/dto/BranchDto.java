package com.bank.publicinfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для отделения банка")
public class BranchDto {

    private Long id;

    @NotBlank(message = "Адрес отделения не может быть пустым")
    @Size(max = 255, message = "Адрес не может превышать 255 символов")
    @Schema(description = "Адрес отделения", example = "г. Москва, ул. Ленина, д. 1")
    private String address;

    @Schema(description = "Телефон отделения", example = "+7 (495) 123-45-67")
    private String phone;

    @Schema(description = "Email отделения", example = "branch@example.com")
    private String email;

    @Schema(description = "Время начала работы", example = "09:00")
    private String startTime;

    @Schema(description = "Время окончания работы", example = "18:00")
    private String endTime;

    @Schema(description = "Тип услуг", example = "Полный спектр")
    private String serviceType;

    @NotNull(message = "ID банка не может быть пустым")
    @Schema(description = "ID банка", example = "1")
    private Long bankDetailsId;
}
