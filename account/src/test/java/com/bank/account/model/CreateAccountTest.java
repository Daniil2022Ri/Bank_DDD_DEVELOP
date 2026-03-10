package com.bank.account.integration;

import com.bank.account.dto.AccountDto;
import com.bank.account.entity.AccountEntity;
import com.bank.account.repository.AccountRepository;
import com.bank.account.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class CreateAccountTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private AccountDto createdAccountDto;
    private Long createdAccountId;

    private static final Integer TEST_ACCOUNT_NUMBER = 999888777;
    private static final Integer TEST_BANK_DETAILS_ID = 9001;
    private static final Integer TEST_PASSPORT_ID = 8001;
    private static final Integer TEST_PROFILE_ID = 7001;
    private static final BigDecimal TEST_MONEY = new BigDecimal("5000.00");
    private static final Boolean TEST_NEGATIVE_BALANCE = false;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        if (createdAccountId != null) {
            accountRepository.deleteById(createdAccountId);
        }
    }

    @Test
    @DisplayName("Интеграционный тест - создание аккаунта с сохранением в БД")
    void createAccount_ShouldSaveToDatabase() {
        AccountDto requestDto = AccountDto.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();

        createdAccountDto = accountService.create(requestDto);
        createdAccountId = createdAccountDto.getId();

        assertNotNull(createdAccountDto);
        assertNotNull(createdAccountDto.getId());
        assertEquals(TEST_ACCOUNT_NUMBER, createdAccountDto.getAccountNumber());
        assertEquals(TEST_BANK_DETAILS_ID, createdAccountDto.getBankDetailsId());
        assertEquals(TEST_MONEY, createdAccountDto.getMoney());
        assertEquals(TEST_PASSPORT_ID, createdAccountDto.getPassportId());
        assertEquals(TEST_PROFILE_ID, createdAccountDto.getProfileId());

        Optional<AccountEntity> savedEntity = accountRepository.findById(createdAccountId);
        assertTrue(savedEntity.isPresent());

        AccountEntity entity = savedEntity.get();
        assertEquals(TEST_ACCOUNT_NUMBER, entity.getAccountNumber());
        assertEquals(TEST_BANK_DETAILS_ID, entity.getBankDetailsId());
        assertEquals(TEST_MONEY, entity.getMoney());
        assertEquals(TEST_PASSPORT_ID, entity.getPassportId());
        assertEquals(TEST_PROFILE_ID, entity.getProfileId());
        assertEquals(TEST_NEGATIVE_BALANCE, entity.getNegativeBalance());
    }

    @Test
    @DisplayName("Интеграционный тест - проверка уникальности номера счета")
    void createAccount_ShouldFail_WhenAccountNumberDuplicate() {
        AccountDto firstAccount = AccountDto.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();


        accountService.create(firstAccount);


        AccountDto duplicateAccount = AccountDto.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(9002)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(8002)
                .profileId(7002)
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.create(duplicateAccount)
        );
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    @DisplayName("Интеграционный тест - проверка всех полей Entity после сохранения")
    void createAccount_ShouldPersistAllEntityFields() {

        AccountDto requestDto = AccountDto.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();


        createdAccountDto = accountService.create(requestDto);
        createdAccountId = createdAccountDto.getId();


        Optional<AccountEntity> savedEntity = accountRepository.findById(createdAccountId);
        assertTrue(savedEntity.isPresent());

        AccountEntity entity = savedEntity.get();


        assertNotNull(entity.getId(), "ID должен быть сгенерирован");
        assertNotNull(entity.getAccountNumber(), "accountNumber не должен быть null");
        assertNotNull(entity.getBankDetailsId(), "bankDetailsId не должен быть null");
        assertNotNull(entity.getMoney(), "money не должен быть null");
        assertNotNull(entity.getNegativeBalance(), "negativeBalance не должен быть null");
        assertNotNull(entity.getPassportId(), "passportId не должен быть null");
        assertNotNull(entity.getProfileId(), "profileId не должен быть null");


        assertEquals(TEST_ACCOUNT_NUMBER, entity.getAccountNumber());
        assertEquals(TEST_BANK_DETAILS_ID, entity.getBankDetailsId());
        assertEquals(TEST_MONEY, entity.getMoney());
        assertEquals(TEST_PASSPORT_ID, entity.getPassportId());
        assertEquals(TEST_PROFILE_ID, entity.getProfileId());
        assertEquals(TEST_NEGATIVE_BALANCE, entity.getNegativeBalance());
    }
}
