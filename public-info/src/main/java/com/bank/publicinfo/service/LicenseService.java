package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.LicenseDto;

import java.util.List;

public interface LicenseService {
    List<LicenseDto> findAll();
    LicenseDto findById(Long id);
    List<LicenseDto> findByBankDetailsId(Long bankDetailsId);
    LicenseDto create(LicenseDto licenseDto);
    LicenseDto update(Long id, LicenseDto licenseDto);
    void delete(Long id);
}
