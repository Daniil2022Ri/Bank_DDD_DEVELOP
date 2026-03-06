package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.model.BankDetailsEntity;
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
public class BankDetailsServiceImpl implements BankDetailsService {

    private final BankDetailsRepository bankDetailsRepository;
    private final BankDetailsMapper bankDetailsMapper;

    @Override
    public List<BankDetailsDto> findAll() {
        log.info("Fetching all bank details");
        return bankDetailsRepository.findAll().stream()
                .map(bankDetailsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BankDetailsDto findById(Long id) {
        log.info("Fetching bank details by id: {}", id);
        BankDetailsEntity entity = bankDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Bank details not found with id: {}", id);
                    return new EntityNotFoundException("Bank details not found with id: " + id);
                });
        return bankDetailsMapper.toDto(entity);
    }

    @Override
    @Transactional
    public BankDetailsDto create(BankDetailsDto bankDetailsDto) {
        log.info("Creating new bank details: {}", bankDetailsDto.getBankName());
        if (bankDetailsRepository.existsByBic(bankDetailsDto.getBic())) {
            log.error("Bank with BIC {} already exists", bankDetailsDto.getBic());
            throw new IllegalArgumentException("Bank with BIC " + bankDetailsDto.getBic() + " already exists");
        }
        BankDetailsEntity entity = bankDetailsMapper.toEntity(bankDetailsDto);
        BankDetailsEntity saved = bankDetailsRepository.save(entity);
        log.info("Bank details created with id: {}", saved.getId());
        return bankDetailsMapper.toDto(saved);
    }

    @Override
    @Transactional
    public BankDetailsDto update(Long id, BankDetailsDto bankDetailsDto) {
        log.info("Updating bank details with id: {}", id);
        BankDetailsEntity existing = bankDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Bank details not found with id: {}", id);
                    return new EntityNotFoundException("Bank details not found with id: " + id);
                });
        
        bankDetailsMapper.updateEntityFromDto(bankDetailsDto, existing);
        BankDetailsEntity updated = bankDetailsRepository.save(existing);
        log.info("Bank details updated with id: {}", updated.getId());
        return bankDetailsMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting bank details with id: {}", id);
        if (!bankDetailsRepository.existsById(id)) {
            log.error("Bank details not found with id: {}", id);
            throw new EntityNotFoundException("Bank details not found with id: " + id);
        }
        bankDetailsRepository.deleteById(id);
        log.info("Bank details deleted with id: {}", id);
    }
}
