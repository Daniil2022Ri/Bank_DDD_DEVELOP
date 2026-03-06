package com.bank.publicinfo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_details", schema = "public_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bic", unique = true, nullable = false)
    @NotBlank(message = "БИК не может быть пустым")
    @Size(min = 9, max = 9, message = "БИК должен содержать 9 символов")
    private String bic;

    @Column(name = "inn", nullable = false)
    @NotBlank(message = "ИНН не может быть пустым")
    @Size(min = 10, max = 12, message = "ИНН должен содержать 10-12 символов")
    private String inn;

    @Column(name = "kpp", nullable = false)
    @NotBlank(message = "КПП не может быть пустым")
    @Size(min = 9, max = 9, message = "КПП должен содержать 9 символов")
    private String kpp;

    @Column(name = "bank_name", nullable = false)
    @NotBlank(message = "Название банка не может быть пустым")
    @Size(max = 255, message = "Название банка не может превышать 255 символов")
    private String bankName;

    @Column(name = "adress")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;
}
