package com.bank.account.dto;

import com.bank.account.util.ApplicationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Учетная запись (счет)")
public class AccountDto {

    @Schema(description = "ID учетной записи", example = "1")
    private Long id;

    @NotNull(message = ApplicationConstants.ERROR_ACCOUNT_NUMBER_NULL)
    @Schema(description = "Номер счета", example = "123456789", required = true)
    private Integer accountNumber;

    @NotNull(message = ApplicationConstants.ERROR_BANK_DETAILS_NULL)
    @Schema(description = "ID банковских реквизитов", example = "1001", required = true)
    private Integer bankDetailsId;

    @PositiveOrZero(message = ApplicationConstants.ERROR_MONEY_NEGATIVE)
    @Schema(description = "Сумма денег на счете", example = "1500.50")
    private BigDecimal money;

    @Schema(description = "Разрешен ли отрицательный баланс", example = "false")
    private Boolean negativeBalance;

    @NotNull(message = ApplicationConstants.ERROR_PASSPORT_NULL)
    @Schema(description = "ID паспорта", example = "5001", required = true)
    private Integer passportId;

    @NotNull(message = ApplicationConstants.ERROR_PROFILE_NULL)
    @Schema(description = "ID профиля", example = "3001", required = true)
    private Integer profileId;
}
