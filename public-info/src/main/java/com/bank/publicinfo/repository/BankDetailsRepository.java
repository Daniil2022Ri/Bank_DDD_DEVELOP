package com.bank.publicinfo.repository;

import com.bank.publicinfo.model.BankDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetailsEntity, Long> {
    Optional<BankDetailsEntity> findByBic(String bic);
    boolean existsByBic(String bic);
}
