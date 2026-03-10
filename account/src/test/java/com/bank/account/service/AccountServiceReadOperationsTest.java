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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceReadOperationsTest {

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
    @DisplayName("findById - успешный поиск по ID")
    void findById_ShouldReturnAccount_WhenExists() {

        when(accountRepository.findById(TEST_ID)).thenReturn(Optional.of(testEntity));
        when(accountMapper.toDto(testEntity)).thenReturn(testDto);


        AccountDto result = accountService.findById(TEST_ID);


        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_ACCOUNT_NUMBER, result.getAccountNumber());
        verify(accountRepository).findById(TEST_ID);
    }

    @Test
    @DisplayName("findById - исключение при не найденном аккаунте")
    void findById_ShouldThrowException_WhenNotFound() {

        when(accountRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> accountService.findById(TEST_ID)
        );
    }

    @Test
    @DisplayName("findByAccountNumber - успешный поиск по номеру счета")
    void findByAccountNumber_ShouldReturnAccount_WhenExists() {
        when(accountRepository.findByAccountNumber(TEST_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(testEntity));
        when(accountMapper.toDto(testEntity)).thenReturn(testDto);

        AccountDto result = accountService.findByAccountNumber(TEST_ACCOUNT_NUMBER);

        assertNotNull(result);
        assertEquals(TEST_ACCOUNT_NUMBER, result.getAccountNumber());
        verify(accountRepository).findByAccountNumber(TEST_ACCOUNT_NUMBER);
    }

    @Test
    @DisplayName("findByAccountNumber - исключение при не найденном аккаунте")
    void findByAccountNumber_ShouldThrowException_WhenNotFound() {

        when(accountRepository.findByAccountNumber(TEST_ACCOUNT_NUMBER))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> accountService.findByAccountNumber(TEST_ACCOUNT_NUMBER)
        );
    }

    @Test
    @DisplayName("findByProfileId - успешный поиск по ID профиля")
    void findByProfileId_ShouldReturnAccount_WhenExists() {

        when(accountRepository.findByProfileId(TEST_PROFILE_ID))
                .thenReturn(Optional.of(testEntity));
        when(accountMapper.toDto(testEntity)).thenReturn(testDto);

        AccountDto result = accountService.findByProfileId(TEST_PROFILE_ID);

        assertNotNull(result);
        assertEquals(TEST_PROFILE_ID, result.getProfileId());
        verify(accountRepository).findByProfileId(TEST_PROFILE_ID);
    }

    @Test
    @DisplayName("findByProfileId - исключение при не найденном аккаунте")
    void findByProfileId_ShouldThrowException_WhenNotFound() {
        when(accountRepository.findByProfileId(TEST_PROFILE_ID))
                .thenReturn(Optional.empty());


        assertThrows(
                EntityNotFoundException.class,
                () -> accountService.findByProfileId(TEST_PROFILE_ID)
        );
    }

    @Test
    @DisplayName("findAll - успешное получение всех аккаунтов")
    void findAll_ShouldReturnAllAccounts() {

        AccountEntity entity2 = AccountEntity.builder()
                .id(2L)
                .accountNumber(987654321)
                .bankDetailsId(1002)
                .money(new BigDecimal("3000.00"))
                .negativeBalance(false)
                .passportId(5002)
                .profileId(3002)
                .build();

        AccountDto dto2 = AccountDto.builder()
                .id(2L)
                .accountNumber(987654321)
                .bankDetailsId(1002)
                .money(new BigDecimal("3000.00"))
                .negativeBalance(false)
                .passportId(5002)
                .profileId(3002)
                .build();

        List<AccountEntity> entities = Arrays.asList(testEntity, entity2);
        List<AccountDto> dtos = Arrays.asList(testDto, dto2);

        when(accountRepository.findAll()).thenReturn(entities);
        when(accountMapper.toDtoList(entities)).thenReturn(dtos);

        List<AccountDto> result = accountService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository).findAll();
    }

    @Test
    @DisplayName("findAll - пустой список при отсутствии аккаунтов")
    void findAll_ShouldReturnEmptyList_WhenNoAccounts() {

        when(accountRepository.findAll()).thenReturn(List.of());
        when(accountMapper.toDtoList(List.of())).thenReturn(List.of());

        List<AccountDto> result = accountService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
