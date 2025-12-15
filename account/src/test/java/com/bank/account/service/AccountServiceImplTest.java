package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import com.bank.account.entity.AccountEntity;
import com.bank.account.exception.EntityNotFoundException;
import com.bank.account.mapper.AccountMapper;
import com.bank.account.repository.AccountRepository;
import com.bank.account.util.ApplicationConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    private static final Long TEST_ACCOUNT_ID = 1L;
    private static final Integer TEST_ACCOUNT_NUMBER = 123456789;
    private static final Integer TEST_BANK_DETAILS_ID = 1001;
    private static final Integer TEST_PASSPORT_ID = 5001;
    private static final Integer TEST_PROFILE_ID = 3001;
    private static final BigDecimal TEST_MONEY_AMOUNT = new BigDecimal("1500.50");
    private static final Boolean TEST_NEGATIVE_BALANCE = false;
    private static final String TEST_ENTITY_NAME = "Account";

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private ApplicationConstants constants;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void create_ShouldCreateAccount_WhenValidData() {
        AccountDto inputDto = createTestAccountDto();
        AccountEntity entity = createTestAccountEntity();
        AccountDto expectedDto = createTestAccountDto();

        when(accountRepository.existsByAccountNumber(TEST_ACCOUNT_NUMBER)).thenReturn(false);
        when(accountMapper.toEntity(inputDto)).thenReturn(entity);
        when(accountRepository.save(entity)).thenReturn(entity);
        when(accountMapper.toDto(entity)).thenReturn(expectedDto);

        AccountDto result = accountService.create(inputDto);

        assertNotNull(result);
        assertEquals(expectedDto.getAccountNumber(), result.getAccountNumber());
        verify(accountRepository).save(entity);
    }

    @Test
    void create_ShouldThrowException_WhenAccountNumberExists() {
        AccountDto inputDto = createTestAccountDto();

        when(accountRepository.existsByAccountNumber(TEST_ACCOUNT_NUMBER)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> accountService.create(inputDto));
        verify(accountRepository, never()).save(any(AccountEntity.class));
    }

    @Test
    void findById_ShouldReturnAccount_WhenExists() {
        AccountEntity entity = createTestAccountEntity();
        AccountDto expectedDto = createTestAccountDto();

        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(entity));
        when(accountMapper.toDto(entity)).thenReturn(expectedDto);
        when(constants.ENTITY_NAME_ACCOUNT).thenReturn(TEST_ENTITY_NAME);

        AccountDto result = accountService.findById(TEST_ACCOUNT_ID);

        assertNotNull(result);
        assertEquals(expectedDto.getAccountNumber(), result.getAccountNumber());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.empty());
        when(constants.ENTITY_NAME_ACCOUNT).thenReturn(TEST_ENTITY_NAME);

        assertThrows(EntityNotFoundException.class, () -> accountService.findById(TEST_ACCOUNT_ID));
    }

    @Test
    void findByAccountNumber_ShouldReturnAccount_WhenExists() {
        AccountEntity entity = createTestAccountEntity();
        AccountDto expectedDto = createTestAccountDto();

        when(accountRepository.findByAccountNumber(TEST_ACCOUNT_NUMBER)).thenReturn(Optional.of(entity));
        when(accountMapper.toDto(entity)).thenReturn(expectedDto);
        when(constants.ENTITY_NAME_ACCOUNT).thenReturn(TEST_ENTITY_NAME);

        AccountDto result = accountService.findByAccountNumber(TEST_ACCOUNT_NUMBER);

        assertNotNull(result);
        assertEquals(expectedDto.getAccountNumber(), result.getAccountNumber());
    }

    @Test
    void findByProfileId_ShouldReturnAccount_WhenExists() {
        AccountEntity entity = createTestAccountEntity();
        AccountDto expectedDto = createTestAccountDto();

        when(accountRepository.findByProfileId(TEST_PROFILE_ID)).thenReturn(Optional.of(entity));
        when(accountMapper.toDto(entity)).thenReturn(expectedDto);
        when(constants.ENTITY_NAME_ACCOUNT).thenReturn(TEST_ENTITY_NAME);

        AccountDto result = accountService.findByProfileId(TEST_PROFILE_ID);

        assertNotNull(result);
        assertEquals(expectedDto.getProfileId(), result.getProfileId());
    }

    @Test
    void findAll_ShouldReturnAllAccounts() {
        List<AccountEntity> entities = Arrays.asList(
                createTestAccountEntity(),
                createTestAccountEntity()
        );
        List<AccountDto> expectedDtos = Arrays.asList(
                createTestAccountDto(),
                createTestAccountDto()
        );

        when(accountRepository.findAll()).thenReturn(entities);
        when(accountMapper.toDtoList(entities)).thenReturn(expectedDtos);

        List<AccountDto> result = accountService.findAll();

        assertNotNull(result);
        assertEquals(entities.size(), result.size());
    }

    @Test
    void delete_ShouldDeleteAccount_WhenExists() {
        when(accountRepository.existsById(TEST_ACCOUNT_ID)).thenReturn(true);
        when(constants.ENTITY_NAME_ACCOUNT).thenReturn(TEST_ENTITY_NAME);

        accountService.delete(TEST_ACCOUNT_ID);

        verify(accountRepository).deleteById(TEST_ACCOUNT_ID);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(accountRepository.existsById(TEST_ACCOUNT_ID)).thenReturn(false);
        when(constants.ENTITY_NAME_ACCOUNT).thenReturn(TEST_ENTITY_NAME);

        assertThrows(EntityNotFoundException.class, () -> accountService.delete(TEST_ACCOUNT_ID));
        verify(accountRepository, never()).deleteById(anyLong());
    }

    private AccountDto createTestAccountDto() {
        return AccountDto.builder()
                .id(TEST_ACCOUNT_ID)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY_AMOUNT)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();
    }

    private AccountEntity createTestAccountEntity() {
        return AccountEntity.builder()
                .id(TEST_ACCOUNT_ID)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY_AMOUNT)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();
    }
}

