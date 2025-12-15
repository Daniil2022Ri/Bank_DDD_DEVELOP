package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import com.bank.account.entity.AccountEntity;
import com.bank.account.exception.EntityNotFoundException;
import com.bank.account.mapper.AccountMapper;
import com.bank.account.repository.AccountRepository;
import com.bank.account.util.ApplicationConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ApplicationConstants constants;

    @Override
    @Transactional
    public AccountDto create(AccountDto accountDto) {
        log.info("Creating new account with account number: {}", accountDto.getAccountNumber());

        if (accountRepository.existsByAccountNumber(accountDto.getAccountNumber())) {
            log.info("Account with number {} already exists", accountDto.getAccountNumber());
            throw new IllegalArgumentException("Account with number " + accountDto.getAccountNumber() + " already exists");
        }

        AccountEntity accountEntity = accountMapper.toEntity(accountDto);
        AccountEntity savedAccount = accountRepository.save(accountEntity);

        log.info("Account created successfully. ID: {}, Account Number: {}",
                savedAccount.getId(), savedAccount.getAccountNumber());

        return accountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto update(Long id, AccountDto accountDto) {
        log.info("Updating account with ID: {}", id);

        AccountEntity existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Account with ID {} not found for update", id);
                    return new EntityNotFoundException(constants.ENTITY_NAME_ACCOUNT, id);
                });

        if (!existingAccount.getAccountNumber().equals(accountDto.getAccountNumber()) &&
                accountRepository.existsByAccountNumber(accountDto.getAccountNumber())) {
            log.info("Account with number {} already exists", accountDto.getAccountNumber());
            throw new IllegalArgumentException("Account with number " + accountDto.getAccountNumber() + " already exists");
        }

        existingAccount.setAccountNumber(accountDto.getAccountNumber());
        existingAccount.setBankDetailsId(accountDto.getBankDetailsId());
        existingAccount.setMoney(accountDto.getMoney());
        existingAccount.setNegativeBalance(accountDto.getNegativeBalance());
        existingAccount.setPassportId(accountDto.getPassportId());
        existingAccount.setProfileId(accountDto.getProfileId());

        AccountEntity updatedAccount = accountRepository.save(existingAccount);

        log.info("Account updated successfully. ID: {}, New Balance: {}",
                updatedAccount.getId(), updatedAccount.getMoney());

        return accountMapper.toDto(updatedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto findById(Long id) {
        log.info("Finding account by ID: {}", id);

        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Account with ID {} not found", id);
                    return new EntityNotFoundException(constants.ENTITY_NAME_ACCOUNT, id);
                });

        return accountMapper.toDto(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto findByAccountNumber(Integer accountNumber) {
        log.info("Finding account by account number: {}", accountNumber);

        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.error("Account with account number {} not found", accountNumber);
                    return new EntityNotFoundException(constants.ENTITY_NAME_ACCOUNT, "accountNumber", accountNumber);
                });

        return accountMapper.toDto(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto findByProfileId(Integer profileId) {
        log.info("Finding account by profile ID: {}", profileId);

        AccountEntity account = accountRepository.findByProfileId(profileId)
                .orElseThrow(() -> {
                    log.error("Account with profile ID {} not found", profileId);
                    return new EntityNotFoundException(constants.ENTITY_NAME_ACCOUNT, "profileId", profileId);
                });

        return accountMapper.toDto(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> findAll() {
        log.info("Finding all accounts");

        List<AccountEntity> accounts = accountRepository.findAll();
        log.info("Found {} accounts", accounts.size());

        return accountMapper.toDtoList(accounts);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting account with ID: {}", id);

        if (!accountRepository.existsById(id)) {
            log.error("Account with ID {} not found for deletion", id);
            throw new EntityNotFoundException(constants.ENTITY_NAME_ACCOUNT, id);
        }

        accountRepository.deleteById(id);
        log.info("Account with ID {} deleted successfully", id);
    }
}

