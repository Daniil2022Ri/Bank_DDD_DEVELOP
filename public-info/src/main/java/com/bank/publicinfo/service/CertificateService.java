package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.CertificateDto;

import java.util.List;

public interface CertificateService {
    List<CertificateDto> findAll();
    CertificateDto findById(Long id);
    List<CertificateDto> findByBankDetailsId(Long bankDetailsId);
    CertificateDto create(CertificateDto certificateDto);
    CertificateDto update(Long id, CertificateDto certificateDto);
    void delete(Long id);
}
