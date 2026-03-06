package com.bank.publicinfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для банковских реквизитов")
public class BankDetailsDto {

    private Long id;

    @NotBlank(message = "БИК не может быть пустым")
    @Pattern(regexp = "^\\d{9}$", message = "БИК должен содержать 9 цифр")
    @Schema(description = "БИК банка", example = "123456789")
    private String bic;

    @NotBlank(message = "ИНН не может быть пустым")
    @Size(min = 10, max = 12, message = "ИНН должен содержать 10-12 символов")
    @Schema(description = "ИНН банка", example = "1234567890")
    private String inn;

    @NotBlank(message = "КПП не может быть пустым")
    @Pattern(regexp = "^\\d{9}$", message = "КПП должен содержать 9 цифр")
    @Schema(description = "КПП банка", example = "123456789")
    private String kpp;

    @NotBlank(message = "Название банка не может быть пустым")
    @Size(max = 255, message = "Название банка не может превышать 255 символов")
    @Schema(description = "Название банка", example = "ПАО Сбербанк")
    private String bankName;

    @Schema(description = "Адрес банка")
    private String address;

    @Schema(description = "Телефон банка")
    private String phone;

    @Schema(description = "Email банка")
    private String email;
}
