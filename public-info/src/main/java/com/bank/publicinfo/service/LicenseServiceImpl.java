package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.model.LicenseEntity;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.LicenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final LicenseMapper licenseMapper;

    @Override
    public List<LicenseDto> findAll() {
        log.info("Fetching all licenses");
        return licenseRepository.findAll().stream()
                .map(licenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LicenseDto findById(Long id) {
        log.info("Fetching license by id: {}", id);
        LicenseEntity entity = licenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("License not found with id: " + id));
        return licenseMapper.toDto(entity);
    }

    @Override
    public List<LicenseDto> findByBankDetailsId(Long bankDetailsId) {
        log.info("Fetching licenses by bank details id: {}", bankDetailsId);
        return licenseRepository.findByBankDetailsId(bankDetailsId).stream()
                .map(licenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LicenseDto create(LicenseDto licenseDto) {
        log.info("Creating new license");
        if (licenseRepository.existsByLicenseNumber(licenseDto.getLicenseNumber())) {
            throw new IllegalArgumentException("License with number " + licenseDto.getLicenseNumber() + " already exists");
        }
        
        BankDetailsEntity bankDetails = bankDetailsRepository.findById(licenseDto.getBankDetailsId())
                .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + licenseDto.getBankDetailsId()));
        
        LicenseEntity entity = licenseMapper.toEntity(licenseDto);
        entity.setBankDetails(bankDetails);
        LicenseEntity saved = licenseRepository.save(entity);
        log.info("License created with id: {}", saved.getId());
        return licenseMapper.toDto(saved);
    }

    @Override
    @Transactional
    public LicenseDto update(Long id, LicenseDto licenseDto) {
        log.info("Updating license with id: {}", id);
        LicenseEntity existing = licenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("License not found with id: " + id));
        
        if (licenseDto.getBankDetailsId() != null) {
            BankDetailsEntity bankDetails = bankDetailsRepository.findById(licenseDto.getBankDetailsId())
                    .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + licenseDto.getBankDetailsId()));
            existing.setBankDetails(bankDetails);
        }
        
        licenseMapper.updateEntityFromDto(licenseDto, existing);
        LicenseEntity updated = licenseRepository.save(existing);
        log.info("License updated with id: {}", updated.getId());
        return licenseMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting license with id: {}", id);
        if (!licenseRepository.existsById(id)) {
            throw new EntityNotFoundException("License not found with id: " + id);
        }
        licenseRepository.deleteById(id);
        log.info("License deleted with id: {}", id);
    }
}
