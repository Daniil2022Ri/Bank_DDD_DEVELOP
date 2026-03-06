package com.bank.publicinfo.repository;

import com.bank.publicinfo.model.LicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<LicenseEntity, Long> {
    Optional<LicenseEntity> findByLicenseNumber(String licenseNumber);
    List<LicenseEntity> findByBankDetailsId(Long bankDetailsId);
    boolean existsByLicenseNumber(String licenseNumber);
}
