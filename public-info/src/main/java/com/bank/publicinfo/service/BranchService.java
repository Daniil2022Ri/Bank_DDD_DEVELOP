package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BranchDto;

import java.util.List;

public interface BranchService {
    List<BranchDto> findAll();
    BranchDto findById(Long id);
    List<BranchDto> findByBankDetailsId(Long bankDetailsId);
    BranchDto create(BranchDto branchDto);
    BranchDto update(Long id, BranchDto branchDto);
    void delete(Long id);
}
