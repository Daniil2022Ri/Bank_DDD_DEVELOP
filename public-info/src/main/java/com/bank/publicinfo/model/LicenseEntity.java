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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "license", schema = "public_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_number", unique = true, nullable = false)
    @NotBlank(message = "Номер лицензии не может быть пустым")
    @Size(max = 50, message = "Номер лицензии не может превышать 50 символов")
    private String licenseNumber;

    @Column(name = "license_type", nullable = false)
    @NotBlank(message = "Тип лицензии не может быть пустым")
    @Size(max = 100, message = "Тип лицензии не может превышать 100 символов")
    private String licenseType;

    @Column(name = "date_of_issue", nullable = false)
    @NotNull(message = "Дата выдачи не может быть пустой")
    private LocalDateTime dateOfIssue;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "bank_details_id", nullable = false)
    @NotNull(message = "ID банка не может быть пустым")
    private BankDetailsEntity bankDetails;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
