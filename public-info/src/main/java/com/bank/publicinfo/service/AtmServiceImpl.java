package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.AtmMapper;
import com.bank.publicinfo.model.AtmEntity;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.repository.BankDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AtmServiceImpl implements AtmService {

    private final AtmRepository atmRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final AtmMapper atmMapper;

    @Override
    public List<AtmDto> findAll() {
        log.info("Fetching all ATMs");
        return atmRepository.findAll().stream()
                .map(atmMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AtmDto findById(Long id) {
        log.info("Fetching ATM by id: {}", id);
        AtmEntity entity = atmRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ATM not found with id: " + id));
        return atmMapper.toDto(entity);
    }

    @Override
    public List<AtmDto> findByBankDetailsId(Long bankDetailsId) {
        log.info("Fetching ATMs by bank details id: {}", bankDetailsId);
        return atmRepository.findByBankDetailsId(bankDetailsId).stream()
                .map(atmMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AtmDto create(AtmDto atmDto) {
        log.info("Creating new ATM");
        BankDetailsEntity bankDetails = bankDetailsRepository.findById(atmDto.getBankDetailsId())
                .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + atmDto.getBankDetailsId()));
        
        AtmEntity entity = atmMapper.toEntity(atmDto);
        entity.setBankDetails(bankDetails);
        AtmEntity saved = atmRepository.save(entity);
        log.info("ATM created with id: {}", saved.getId());
        return atmMapper.toDto(saved);
    }

    @Override
    @Transactional
    public AtmDto update(Long id, AtmDto atmDto) {
        log.info("Updating ATM with id: {}", id);
        AtmEntity existing = atmRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ATM not found with id: " + id));
        
        if (atmDto.getBankDetailsId() != null) {
            BankDetailsEntity bankDetails = bankDetailsRepository.findById(atmDto.getBankDetailsId())
                    .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + atmDto.getBankDetailsId()));
            existing.setBankDetails(bankDetails);
        }
        
        atmMapper.updateEntityFromDto(atmDto, existing);
        AtmEntity updated = atmRepository.save(existing);
        log.info("ATM updated with id: {}", updated.getId());
        return atmMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting ATM with id: {}", id);
        if (!atmRepository.existsById(id)) {
            throw new EntityNotFoundException("ATM not found with id: " + id);
        }
        atmRepository.deleteById(id);
        log.info("ATM deleted with id: {}", id);
    }
}
