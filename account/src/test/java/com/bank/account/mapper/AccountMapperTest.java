package com.bank.account.mapper;

import com.bank.account.dto.AccountDto;
import com.bank.account.entity.AccountEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    private static final Long TEST_ID = 1L;
    private static final Integer TEST_ACCOUNT_NUMBER = 123456789;
    private static final Integer TEST_BANK_DETAILS_ID = 1001;
    private static final Integer TEST_PASSPORT_ID = 5001;
    private static final Integer TEST_PROFILE_ID = 3001;
    private static final BigDecimal TEST_MONEY = new BigDecimal("1500.50");
    private static final Boolean TEST_NEGATIVE_BALANCE = false;

    @Test
    @DisplayName("toEntity - конвертация DTO в Entity")
    void toEntity_ShouldConvertDtoToEntity() {

        AccountDto dto = AccountDto.builder()
                .id(TEST_ID)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();


        AccountEntity entity = accountMapper.toEntity(dto);


        assertNotNull(entity);
        assertEquals(TEST_ID, entity.getId());
        assertEquals(TEST_ACCOUNT_NUMBER, entity.getAccountNumber());
        assertEquals(TEST_BANK_DETAILS_ID, entity.getBankDetailsId());
        assertEquals(TEST_MONEY, entity.getMoney());
        assertEquals(TEST_NEGATIVE_BALANCE, entity.getNegativeBalance());
        assertEquals(TEST_PASSPORT_ID, entity.getPassportId());
        assertEquals(TEST_PROFILE_ID, entity.getProfileId());
    }

    @Test
    @DisplayName("toDto - конвертация Entity в DTO")
    void toDto_ShouldConvertEntityToDto() {

        AccountEntity entity = AccountEntity.builder()
                .id(TEST_ID)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();


        AccountDto dto = accountMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(TEST_ID, dto.getId());
        assertEquals(TEST_ACCOUNT_NUMBER, dto.getAccountNumber());
        assertEquals(TEST_BANK_DETAILS_ID, dto.getBankDetailsId());
        assertEquals(TEST_MONEY, dto.getMoney());
        assertEquals(TEST_NEGATIVE_BALANCE, dto.getNegativeBalance());
        assertEquals(TEST_PASSPORT_ID, dto.getPassportId());
        assertEquals(TEST_PROFILE_ID, dto.getProfileId());
    }
}
