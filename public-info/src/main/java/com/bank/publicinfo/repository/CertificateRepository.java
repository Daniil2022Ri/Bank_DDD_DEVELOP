package com.bank.publicinfo.repository;

import com.bank.publicinfo.model.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateEntity, Long> {
    Optional<CertificateEntity> findByCertificateNumber(String certificateNumber);
    List<CertificateEntity> findByBankDetailsId(Long bankDetailsId);
    boolean existsByCertificateNumber(String certificateNumber);
}
