package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.model.BankDetailsEntity;
import com.bank.publicinfo.model.BranchEntity;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final BranchMapper branchMapper;

    @Override
    public List<BranchDto> findAll() {
        log.info("Fetching all branches");
        return branchRepository.findAll().stream()
                .map(branchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BranchDto findById(Long id) {
        log.info("Fetching branch by id: {}", id);
        BranchEntity entity = branchRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Branch not found with id: {}", id);
                    return new EntityNotFoundException("Branch not found with id: " + id);
                });
        return branchMapper.toDto(entity);
    }

    @Override
    public List<BranchDto> findByBankDetailsId(Long bankDetailsId) {
        log.info("Fetching branches by bank details id: {}", bankDetailsId);
        return branchRepository.findByBankDetailsId(bankDetailsId).stream()
                .map(branchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BranchDto create(BranchDto branchDto) {
        log.info("Creating new branch");
        BankDetailsEntity bankDetails = bankDetailsRepository.findById(branchDto.getBankDetailsId())
                .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + branchDto.getBankDetailsId()));
        
        BranchEntity entity = branchMapper.toEntity(branchDto);
        entity.setBankDetails(bankDetails);
        BranchEntity saved = branchRepository.save(entity);
        log.info("Branch created with id: {}", saved.getId());
        return branchMapper.toDto(saved);
    }

    @Override
    @Transactional
    public BranchDto update(Long id, BranchDto branchDto) {
        log.info("Updating branch with id: {}", id);
        BranchEntity existing = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + id));
        
        if (branchDto.getBankDetailsId() != null) {
            BankDetailsEntity bankDetails = bankDetailsRepository.findById(branchDto.getBankDetailsId())
                    .orElseThrow(() -> new EntityNotFoundException("Bank not found with id: " + branchDto.getBankDetailsId()));
            existing.setBankDetails(bankDetails);
        }
        
        branchMapper.updateEntityFromDto(branchDto, existing);
        BranchEntity updated = branchRepository.save(existing);
        log.info("Branch updated with id: {}", updated.getId());
        return branchMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting branch with id: {}", id);
        if (!branchRepository.existsById(id)) {
            throw new EntityNotFoundException("Branch not found with id: " + id);
        }
        branchRepository.deleteById(id);
        log.info("Branch deleted with id: {}", id);
    }
}
