package com.bank.publicinfo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "atm", schema = "public_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Адрес банкомата не может быть пустым")
    @Size(max = 255, message = "Адрес не может превышать 255 символов")
    private String address;

    @Column(name = "atm_name")
    @Size(max = 100, message = "Название банкомата не может превышать 100 символов")
    private String atmName;

    @Column(name = "all_day")
    private Boolean allDay;

    @Column(name = "availability")
    private Boolean availability;

    @ManyToOne
    @JoinColumn(name = "bank_details_id", nullable = false)
    @NotNull(message = "ID банка не может быть пустым")
    private BankDetailsEntity bankDetails;
}
