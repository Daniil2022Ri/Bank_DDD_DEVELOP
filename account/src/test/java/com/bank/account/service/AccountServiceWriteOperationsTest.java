package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import com.bank.account.entity.AccountEntity;
import com.bank.account.exception.EntityNotFoundException;
import com.bank.account.mapper.AccountMapper;
import com.bank.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceWriteOperationsTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private static final Long TEST_ID = 1L;
    private static final Integer TEST_ACCOUNT_NUMBER = 123456789;
    private static final Integer TEST_BANK_DETAILS_ID = 1001;
    private static final Integer TEST_PASSPORT_ID = 5001;
    private static final Integer TEST_PROFILE_ID = 3001;
    private static final BigDecimal TEST_MONEY = new BigDecimal("1500.50");
    private static final Boolean TEST_NEGATIVE_BALANCE = false;

    private AccountDto testDto;
    private AccountEntity testEntity;

    @BeforeEach
    void setUp() {
        testDto = AccountDto.builder()
                .id(TEST_ID)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();

        testEntity = AccountEntity.builder()
                .id(TEST_ID)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();
    }

    @Test
    @DisplayName("create - успешное создание аккаунта")
    void create_ShouldCreateAccount_WhenValidData() {

        when(accountRepository.existsByAccountNumber(TEST_ACCOUNT_NUMBER)).thenReturn(false);
        when(accountMapper.toEntity(testDto)).thenReturn(testEntity);
        when(accountRepository.save(testEntity)).thenReturn(testEntity);
        when(accountMapper.toDto(testEntity)).thenReturn(testDto);


        AccountDto result = accountService.create(testDto);


        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_ACCOUNT_NUMBER, result.getAccountNumber());
        verify(accountRepository).save(testEntity);
        verify(accountMapper).toEntity(testDto);
        verify(accountMapper).toDto(testEntity);
    }

    @Test
    @DisplayName("create - исключение при существующем номере счета")
    void create_ShouldThrowException_WhenAccountNumberExists() {

        when(accountRepository.existsByAccountNumber(TEST_ACCOUNT_NUMBER)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.create(testDto)
        );
        assertTrue(exception.getMessage().contains("already exists"));
        verify(accountRepository, never()).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("update - успешное обновление аккаунта")
    void update_ShouldUpdateAccount_WhenValidData() {

        AccountDto updateDto = AccountDto.builder()
                .accountNumber(987654321)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(new BigDecimal("2000.00"))
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();

        when(accountRepository.findById(TEST_ID)).thenReturn(Optional.of(testEntity));
        when(accountRepository.existsByAccountNumber(987654321)).thenReturn(false);
        when(accountRepository.save(testEntity)).thenReturn(testEntity);
        when(accountMapper.toDto(testEntity)).thenReturn(testDto);


        AccountDto result = accountService.update(TEST_ID, updateDto);


        assertNotNull(result);
        verify(accountRepository).findById(TEST_ID);
        verify(accountRepository).save(testEntity);
    }

    @Test
    @DisplayName("update - исключение при не найденном аккаунте")
    void update_ShouldThrowException_WhenAccountNotFound() {

        when(accountRepository.findById(TEST_ID)).thenReturn(Optional.empty());


        assertThrows(
                EntityNotFoundException.class,
                () -> accountService.update(TEST_ID, testDto)
        );
        verify(accountRepository, never()).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("update - исключение при конфликте номера счета")
    void update_ShouldThrowException_WhenNewAccountNumberExists() {

        AccountDto updateDto = AccountDto.builder()
                .accountNumber(987654321)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();

        testEntity.setAccountNumber(111111111);
        when(accountRepository.findById(TEST_ID)).thenReturn(Optional.of(testEntity));
        when(accountRepository.existsByAccountNumber(987654321)).thenReturn(true);


        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.update(TEST_ID, updateDto)
        );
        verify(accountRepository, never()).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("delete - успешное удаление аккаунта")
    void delete_ShouldDeleteAccount_WhenExists() {

        when(accountRepository.existsById(TEST_ID)).thenReturn(true);


        accountService.delete(TEST_ID);


        verify(accountRepository).deleteById(TEST_ID);
    }

    @Test
    @DisplayName("delete - исключение при не найденном аккаунте")
    void delete_ShouldThrowException_WhenAccountNotFound() {

        when(accountRepository.existsById(TEST_ID)).thenReturn(false);


        assertThrows(
                EntityNotFoundException.class,
                () -> accountService.delete(TEST_ID)
        );
        verify(accountRepository, never()).deleteById(anyLong());
    }
}
