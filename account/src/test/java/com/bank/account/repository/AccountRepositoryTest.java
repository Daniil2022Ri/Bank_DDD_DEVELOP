package com.bank.account.repository;

import com.bank.account.entity.AccountEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("local")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    private static final Integer TEST_ACCOUNT_NUMBER = 123456789;
    private static final Integer TEST_BANK_DETAILS_ID = 1001;
    private static final Integer TEST_PASSPORT_ID = 5001;
    private static final Integer TEST_PROFILE_ID = 3001;
    private static final BigDecimal TEST_MONEY = new BigDecimal("1500.50");
    private static final Boolean TEST_NEGATIVE_BALANCE = false;

    private AccountEntity savedAccount;

    @BeforeEach
    void setUp() {
        AccountEntity account = AccountEntity.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();

        savedAccount = entityManager.persistAndFlush(account);
    }

    @AfterEach
    void tearDown() {
        if (savedAccount != null && savedAccount.getId() != null) {
            accountRepository.deleteById(savedAccount.getId());
        }
    }

    @Test
    @DisplayName("findByAccountNumber - находит аккаунт по номеру счета")
    void findByAccountNumber_ShouldReturnAccount_WhenExists() {
        Optional<AccountEntity> result = accountRepository.findByAccountNumber(TEST_ACCOUNT_NUMBER);

        assertTrue(result.isPresent());
        assertEquals(TEST_ACCOUNT_NUMBER, result.get().getAccountNumber());
        assertEquals(TEST_BANK_DETAILS_ID, result.get().getBankDetailsId());
        assertEquals(TEST_MONEY, result.get().getMoney());
    }

    @Test
    @DisplayName("findByAccountNumber - возвращает пустой Optional когда аккаунт не найден")
    void findByAccountNumber_ShouldReturnEmpty_WhenNotExists() {
        Optional<AccountEntity> result = accountRepository.findByAccountNumber(999999999);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("existsByAccountNumber - возвращает true когда аккаунт существует")
    void existsByAccountNumber_ShouldReturnTrue_WhenExists() {
        boolean exists = accountRepository.existsByAccountNumber(TEST_ACCOUNT_NUMBER);

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByAccountNumber - возвращает false когда аккаунт не существует")
    void existsByAccountNumber_ShouldReturnFalse_WhenNotExists() {
        boolean exists = accountRepository.existsByAccountNumber(999999999);

        assertFalse(exists);
    }

    @Test
    @DisplayName("findByProfileId - находит аккаунт по ID профиля")
    void findByProfileId_ShouldReturnAccount_WhenExists() {
        Optional<AccountEntity> result = accountRepository.findByProfileId(TEST_PROFILE_ID);

        assertTrue(result.isPresent());
        assertEquals(TEST_PROFILE_ID, result.get().getProfileId());
        assertEquals(TEST_ACCOUNT_NUMBER, result.get().getAccountNumber());
    }

    @Test
    @DisplayName("findByProfileId - возвращает пустой Optional когда аккаунт не найден")
    void findByProfileId_ShouldReturnEmpty_WhenNotExists() {
        Optional<AccountEntity> result = accountRepository.findByProfileId(9999);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findById - находит аккаунт по ID")
    void findById_ShouldReturnAccount_WhenExists() {
        Optional<AccountEntity> result = accountRepository.findById(savedAccount.getId());

        assertTrue(result.isPresent());
        assertEquals(savedAccount.getId(), result.get().getId());
        assertEquals(TEST_ACCOUNT_NUMBER, result.get().getAccountNumber());
    }

    @Test
    @DisplayName("save - сохраняет новый аккаунт")
    void save_ShouldPersistAccount() {
        AccountEntity newAccount = AccountEntity.builder()
                .accountNumber(987654321)
                .bankDetailsId(1002)
                .money(new BigDecimal("2000.00"))
                .negativeBalance(false)
                .passportId(5002)
                .profileId(3002)
                .build();

        AccountEntity saved = accountRepository.save(newAccount);

        assertNotNull(saved.getId());
        assertEquals(987654321, saved.getAccountNumber());

        Optional<AccountEntity> found = accountRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(987654321, found.get().getAccountNumber());
    }

    @Test
    @DisplayName("deleteById - удаляет аккаунт")
    void deleteById_ShouldRemoveAccount() {
        Long accountId = savedAccount.getId();

        accountRepository.deleteById(accountId);

        Optional<AccountEntity> result = accountRepository.findById(accountId);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAll - возвращает все аккаунты")
    void findAll_ShouldReturnAllAccounts() {
        AccountEntity secondAccount = AccountEntity.builder()
                .accountNumber(987654321)
                .bankDetailsId(1002)
                .money(new BigDecimal("2000.00"))
                .negativeBalance(false)
                .passportId(5002)
                .profileId(3002)
                .build();

        entityManager.persistAndFlush(secondAccount);

        var accounts = accountRepository.findAll();

        assertTrue(accounts.size() >= 2);
    }

    @Test
    @DisplayName("count - возвращает количество аккаунтов")
    void count_ShouldReturnAccountCount() {
        long count = accountRepository.count();

        assertTrue(count >= 1);
    }
}
