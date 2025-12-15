package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "Phone_transfers")
public class SuspiciousPhoneTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Phone_id")
    private Long id;

    @Column(name = "is_blocked")
    private boolean blocked;

    @Column(name = "is_suspicious")
    private boolean suspicious;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @Column(name = "suspicious_reason")
    private String suspiciousReason;

}
