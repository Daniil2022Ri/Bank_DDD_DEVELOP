package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BankDetailsDto;

import java.util.List;

public interface BankDetailsService {
    List<BankDetailsDto> findAll();
    BankDetailsDto findById(Long id);
    BankDetailsDto create(BankDetailsDto bankDetailsDto);
    BankDetailsDto update(Long id, BankDetailsDto bankDetailsDto);
    void delete(Long id);
}
