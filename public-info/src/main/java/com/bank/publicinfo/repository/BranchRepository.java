package com.bank.publicinfo.repository;

import com.bank.publicinfo.model.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Long> {
    List<BranchEntity> findByBankDetailsId(Long bankDetailsId);
}
