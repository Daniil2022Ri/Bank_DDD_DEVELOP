package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.CertificateMapper;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.model.CertificateEntity;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final CertificateMapper certificateMapper;

    @Override
    public List<CertificateDto> findAll() {
        log.info("Fetching all certificates");
        return certificateRepository.findAll().stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateDto findById(Long id) {
        log.info("Fetching certificate by id: {}", id);
        CertificateEntity entity = certificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate not found with id: " + id));
        return certificateMapper.toDto(entity);
    }

    @Override
    public List<CertificateDto> findByBankDetailsId(Long bankDetailsId) {
        log.info("Fetching certificates by bank details id: {}", bankDetailsId);
        return certificateRepository.findByBankDetailsId(bankDetailsId).stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CertificateDto create(CertificateDto certificateDto) {
        log.info("Creating new certificate");
        if (certificateRepository.existsByCertificateNumber(certificateDto.getCertificateNumber())) {
            throw new IllegalArgumentException("Certificate with number " + certificateDto.getCertificateNumber() + " already exists");
        }
        
        BankDetailsEntity bankDetails = bankDetailsRepository.findById(certificateDto.getBankDetailsId())
                .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + certificateDto.getBankDetailsId()));
        
        CertificateEntity entity = certificateMapper.toEntity(certificateDto);
        entity.setBankDetails(bankDetails);
        CertificateEntity saved = certificateRepository.save(entity);
        log.info("Certificate created with id: {}", saved.getId());
        return certificateMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CertificateDto update(Long id, CertificateDto certificateDto) {
        log.info("Updating certificate with id: {}", id);
        CertificateEntity existing = certificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate not found with id: " + id));
        
        if (certificateDto.getBankDetailsId() != null) {
            BankDetailsEntity bankDetails = bankDetailsRepository.findById(certificateDto.getBankDetailsId())
                    .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + certificateDto.getBankDetailsId()));
            existing.setBankDetails(bankDetails);
        }
        
        certificateMapper.updateEntityFromDto(certificateDto, existing);
        CertificateEntity updated = certificateRepository.save(existing);
        log.info("Certificate updated with id: {}", updated.getId());
        return certificateMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting certificate with id: {}", id);
        if (!certificateRepository.existsById(id)) {
            throw new EntityNotFoundException("Certificate not found with id: " + id);
        }
        certificateRepository.deleteById(id);
        log.info("Certificate deleted with id: {}", id);
    }
}
