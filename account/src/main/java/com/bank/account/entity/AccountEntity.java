package com.bank.account.entity;

import com.bank.account.util.ApplicationConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "account_details", schema = "account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    @NotNull(message = ApplicationConstants.ERROR_ACCOUNT_NUMBER_NULL)
    private Integer accountNumber;

    @Column(name = "bank_details_id", unique = true, nullable = false)
    @NotNull(message = ApplicationConstants.ERROR_BANK_DETAILS_NULL)
    private Integer bankDetailsId;

    @Column(name = "money", precision = 15, scale = 2)
    @PositiveOrZero(message = ApplicationConstants.ERROR_MONEY_NEGATIVE)
    private BigDecimal money;

    @Column(name = "negative_balance")
    private Boolean negativeBalance;

    @Column(name = "passport_id", nullable = false)
    @NotNull(message = ApplicationConstants.ERROR_PASSPORT_NULL)
    private Integer passportId;

    @Column(name = "profile_id", nullable = false)
    @NotNull(message = ApplicationConstants.ERROR_PROFILE_NULL)
    private Integer profileId;
}
