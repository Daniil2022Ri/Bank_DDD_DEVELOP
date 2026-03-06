package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AtmDto;

import java.util.List;

public interface AtmService {
    List<AtmDto> findAll();
    AtmDto findById(Long id);
    List<AtmDto> findByBankDetailsId(Long bankDetailsId);
    AtmDto create(AtmDto atmDto);
    AtmDto update(Long id, AtmDto atmDto);
    void delete(Long id);
}
