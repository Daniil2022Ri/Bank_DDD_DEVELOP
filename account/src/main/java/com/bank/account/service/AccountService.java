package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import java.util.List;

public interface AccountService {
    AccountDto create(AccountDto accountDto);
    AccountDto update(Long id, AccountDto accountDto);
    AccountDto findById(Long id);
    AccountDto findByAccountNumber(Integer accountNumber);
    AccountDto findByProfileId(Integer profileId);
    List<AccountDto> findAll();
    void delete(Long id);
}

