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
@Schema(description = "DTO для банкомата")
public class AtmDto {

    private Long id;

    @NotBlank(message = "Адрес банкомата не может быть пустым")
    @Size(max = 255, message = "Адрес не может превышать 255 символов")
    @Schema(description = "Адрес банкомата", example = "г. Москва, ул. Ленина, д. 1")
    private String address;

    @Size(max = 100, message = "Название банкомата не может превышать 100 символов")
    @Schema(description = "Название банкомата", example = "ATM-001")
    private String atmName;

    @Schema(description = "Круглосуточный режим работы")
    private Boolean allDay;

    @Schema(description = "Доступность банкомата")
    private Boolean availability;

    @NotNull(message = "ID банка не может быть пустым")
    @Schema(description = "ID банка", example = "1")
    private Long bankDetailsId;
}
