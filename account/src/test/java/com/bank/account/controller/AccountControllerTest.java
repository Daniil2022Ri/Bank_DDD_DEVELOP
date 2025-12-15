package com.bank.account.controller;

import com.bank.account.dto.AccountDto;
import com.bank.account.exception.EntityNotFoundException;
import com.bank.account.service.AccountService;
import com.bank.account.util.ApplicationConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    private static final String TEST_BASE_URL = "/accounts";
    private static final Long TEST_ACCOUNT_ID = 1L;
    private static final Long TEST_SECOND_ACCOUNT_ID = 2L;
    private static final Integer TEST_ACCOUNT_NUMBER = 123456;
    private static final Integer TEST_UPDATED_ACCOUNT_NUMBER = 123457;
    private static final Integer TEST_BANK_DETAILS_ID = 1001;
    private static final Integer TEST_PASSPORT_ID = 5001;
    private static final Integer TEST_PROFILE_ID = 3001;
    private static final Integer TEST_UPDATED_PROFILE_ID = 3002;
    private static final BigDecimal TEST_MONEY_AMOUNT = new BigDecimal("1500.50");
    private static final BigDecimal TEST_UPDATED_MONEY_AMOUNT = new BigDecimal("2000.00");
    private static final BigDecimal TEST_NEGATIVE_MONEY_AMOUNT = new BigDecimal("-100.00");
    private static final BigDecimal TEST_SMALL_MONEY_AMOUNT = new BigDecimal("100.00");
    private static final Boolean TEST_NEGATIVE_BALANCE = Boolean.FALSE;

    private static final String TEST_JSON_PATH_ID = "$.id";
    private static final String TEST_JSON_PATH_ACCOUNT_NUMBER = "$.accountNumber";
    private static final String TEST_JSON_PATH_PROFILE_ID = "$.profileId";
    private static final String TEST_JSON_PATH_MONEY = "$.money";
    private static final String TEST_JSON_PATH_STATUS = "$.status";
    private static final String TEST_JSON_PATH_ERROR = "$.error";
    private static final String TEST_JSON_PATH_ERRORS_ACCOUNT_NUMBER = "$.errors.accountNumber";
    private static final String TEST_JSON_PATH_ERRORS_BANK_DETAILS_ID = "$.errors.bankDetailsId";
    private static final String TEST_JSON_PATH_ERRORS_MONEY = "$.errors.money";
    private static final String TEST_JSON_PATH_ERRORS_PASSPORT_ID = "$.errors.passportId";
    private static final String TEST_JSON_PATH_ERRORS_PROFILE_ID = "$.errors.profileId";
    private static final String TEST_JSON_PATH_ARRAY_SIZE = "$";
    private static final String TEST_JSON_PATH_FIRST_ELEMENT_ID = "$[0].id";
    private static final String TEST_JSON_PATH_SECOND_ELEMENT_ID = "$[1].id";

    private static final int TEST_STATUS_BAD_REQUEST = 400;
    private static final int TEST_STATUS_NOT_FOUND = 404;

    private static final String TEST_ERROR_KEY_NOT_FOUND = "Not Found";
    private static final String TEST_ERROR_KEY_VALIDATION_FAILED = "Validation Failed";
    private static final String TEST_ERROR_KEY_ACCOUNT_NUMBER_NULL = "Account number cannot be null";
    private static final String TEST_ERROR_KEY_BANK_DETAILS_NULL = "Bank details ID cannot be null";
    private static final String TEST_ERROR_KEY_MONEY_NEGATIVE = "Money amount cannot be negative";
    private static final String TEST_ERROR_KEY_PASSPORT_NULL = "Passport ID cannot be null";
    private static final String TEST_ERROR_KEY_PROFILE_NULL = "Profile ID cannot be null";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ApplicationConstants constants;

    @BeforeEach
    void setUp() {
        given(constants.getErrorNotFound()).willReturn(TEST_ERROR_KEY_NOT_FOUND);
        given(constants.getErrorValidationFailed()).willReturn(TEST_ERROR_KEY_VALIDATION_FAILED);
        given(constants.getErrorAccountNumberNull()).willReturn(TEST_ERROR_KEY_ACCOUNT_NUMBER_NULL);
        given(constants.getErrorBankDetailsNull()).willReturn(TEST_ERROR_KEY_BANK_DETAILS_NULL);
        given(constants.getErrorMoneyNegative()).willReturn(TEST_ERROR_KEY_MONEY_NEGATIVE);
        given(constants.getErrorPassportNull()).willReturn(TEST_ERROR_KEY_PASSPORT_NULL);
        given(constants.getErrorProfileNull()).willReturn(TEST_ERROR_KEY_PROFILE_NULL);
    }

    @Test
    @DisplayName("POST /accounts - успешно создает аккаунт")
    void create_ShouldReturnCreatedAccount() throws Exception {
        AccountDto requestDto = buildAccountDto(null);
        AccountDto responseDto = buildAccountDto(TEST_ACCOUNT_ID);

        given(accountService.create(any(AccountDto.class))).willReturn(responseDto);

        mockMvc.perform(post(TEST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(TEST_JSON_PATH_ID, is(TEST_ACCOUNT_ID.intValue())))
                .andExpect(jsonPath(TEST_JSON_PATH_ACCOUNT_NUMBER, is(TEST_ACCOUNT_NUMBER)));
    }

    @Test
    @DisplayName("GET /accounts/{id} - возвращает аккаунт по ID")
    void getById_ShouldReturnAccount() throws Exception {
        AccountDto responseDto = buildAccountDto(TEST_ACCOUNT_ID);

        given(accountService.findById(TEST_ACCOUNT_ID)).willReturn(responseDto);

        mockMvc.perform(get(TEST_BASE_URL + "/{id}", TEST_ACCOUNT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TEST_JSON_PATH_ID, is(TEST_ACCOUNT_ID.intValue())))
                .andExpect(jsonPath(TEST_JSON_PATH_ACCOUNT_NUMBER, is(TEST_ACCOUNT_NUMBER)));
    }

    @Test
    @DisplayName("GET /accounts/{id} - 404 если аккаунт не найден")
    void getById_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        given(accountService.findById(TEST_ACCOUNT_ID))
                .willThrow(new EntityNotFoundException("Account", TEST_ACCOUNT_ID));

        mockMvc.perform(get(TEST_BASE_URL + "/{id}", TEST_ACCOUNT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(TEST_JSON_PATH_STATUS, is(TEST_STATUS_NOT_FOUND)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERROR, is(TEST_ERROR_KEY_NOT_FOUND)));
    }

    @Test
    @DisplayName("GET /accounts/by-account-number/{accountNumber} - возвращает аккаунт по номеру счета")
    void getByAccountNumber_ShouldReturnAccount() throws Exception {
        AccountDto responseDto = buildAccountDto(TEST_ACCOUNT_ID);

        given(accountService.findByAccountNumber(TEST_ACCOUNT_NUMBER)).willReturn(responseDto);

        mockMvc.perform(get(TEST_BASE_URL + "/by-account-number/{accountNumber}", TEST_ACCOUNT_NUMBER))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TEST_JSON_PATH_ACCOUNT_NUMBER, is(TEST_ACCOUNT_NUMBER)));
    }

    @Test
    @DisplayName("GET /accounts/by-profile/{profileId} - возвращает аккаунт по profileId")
    void getByProfileId_ShouldReturnAccount() throws Exception {
        AccountDto responseDto = buildAccountDto(TEST_ACCOUNT_ID);

        given(accountService.findByProfileId(TEST_PROFILE_ID)).willReturn(responseDto);

        mockMvc.perform(get(TEST_BASE_URL + "/by-profile/{profileId}", TEST_PROFILE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TEST_JSON_PATH_PROFILE_ID, is(TEST_PROFILE_ID)));
    }

    @Test
    @DisplayName("GET /accounts - возвращает список аккаунтов")
    void getAll_ShouldReturnAccounts() throws Exception {
        AccountDto firstAccount = buildAccountDto(TEST_ACCOUNT_ID);
        AccountDto secondAccount = buildAccountDto(TEST_SECOND_ACCOUNT_ID);
        secondAccount.setAccountNumber(TEST_UPDATED_ACCOUNT_NUMBER);
        secondAccount.setProfileId(TEST_UPDATED_PROFILE_ID);

        given(accountService.findAll()).willReturn(List.of(firstAccount, secondAccount));

        mockMvc.perform(get(TEST_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TEST_JSON_PATH_ARRAY_SIZE, hasSize(2)))
                .andExpect(jsonPath(TEST_JSON_PATH_FIRST_ELEMENT_ID, is(TEST_ACCOUNT_ID.intValue())))
                .andExpect(jsonPath(TEST_JSON_PATH_SECOND_ELEMENT_ID, is(TEST_SECOND_ACCOUNT_ID.intValue())));
    }

    @Test
    @DisplayName("PUT /accounts/{id} - обновляет аккаунт")
    void update_ShouldReturnUpdatedAccount() throws Exception {
        AccountDto requestDto = buildAccountDto(null);
        requestDto.setMoney(TEST_UPDATED_MONEY_AMOUNT);

        AccountDto responseDto = buildAccountDto(TEST_ACCOUNT_ID);
        responseDto.setMoney(TEST_UPDATED_MONEY_AMOUNT);

        given(accountService.update(eq(TEST_ACCOUNT_ID), any(AccountDto.class))).willReturn(responseDto);

        mockMvc.perform(put(TEST_BASE_URL + "/{id}", TEST_ACCOUNT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TEST_JSON_PATH_ID, is(TEST_ACCOUNT_ID.intValue())))
                .andExpect(jsonPath(TEST_JSON_PATH_MONEY, is(TEST_UPDATED_MONEY_AMOUNT.doubleValue())));
    }

    @Test
    @DisplayName("DELETE /accounts/{id} - успешно удаляет аккаунт")
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(accountService).delete(TEST_ACCOUNT_ID);

        mockMvc.perform(delete(TEST_BASE_URL + "/{id}", TEST_ACCOUNT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /accounts/{id} - 404 если аккаунт не найден")
    void delete_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        doThrow(new EntityNotFoundException("Account", TEST_ACCOUNT_ID))
                .when(accountService).delete(TEST_ACCOUNT_ID);

        mockMvc.perform(delete(TEST_BASE_URL + "/{id}", TEST_ACCOUNT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(TEST_JSON_PATH_STATUS, is(TEST_STATUS_NOT_FOUND)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERROR, is(TEST_ERROR_KEY_NOT_FOUND)));
    }

    @Test
    @DisplayName("POST /accounts - 400 при валидации (accountNumber null)")
    void create_ShouldReturnBadRequest_WhenAccountNumberIsNull() throws Exception {
        AccountDto requestDto = AccountDto.builder()
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .money(TEST_SMALL_MONEY_AMOUNT)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .build();

        mockMvc.perform(post(TEST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(TEST_JSON_PATH_STATUS, is(TEST_STATUS_BAD_REQUEST)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERROR, is(TEST_ERROR_KEY_VALIDATION_FAILED)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERRORS_ACCOUNT_NUMBER, is(TEST_ERROR_KEY_ACCOUNT_NUMBER_NULL)));
    }

    @Test
    @DisplayName("POST /accounts - 400 при валидации (bankDetailsId null)")
    void create_ShouldReturnBadRequest_WhenBankDetailsIdIsNull() throws Exception {
        AccountDto requestDto = AccountDto.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .money(TEST_SMALL_MONEY_AMOUNT)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .build();

        mockMvc.perform(post(TEST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(TEST_JSON_PATH_STATUS, is(TEST_STATUS_BAD_REQUEST)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERROR, is(TEST_ERROR_KEY_VALIDATION_FAILED)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERRORS_BANK_DETAILS_ID, is(TEST_ERROR_KEY_BANK_DETAILS_NULL)));
    }

    @Test
    @DisplayName("POST /accounts - 400 при валидации (negative money)")
    void create_ShouldReturnBadRequest_WhenMoneyIsNegative() throws Exception {
        AccountDto requestDto = buildAccountDto(null);
        requestDto.setMoney(TEST_NEGATIVE_MONEY_AMOUNT);

        mockMvc.perform(post(TEST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(TEST_JSON_PATH_STATUS, is(TEST_STATUS_BAD_REQUEST)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERROR, is(TEST_ERROR_KEY_VALIDATION_FAILED)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERRORS_MONEY, is(TEST_ERROR_KEY_MONEY_NEGATIVE)));
    }

    @Test
    @DisplayName("POST /accounts - 400 при валидации (passportId null)")
    void create_ShouldReturnBadRequest_WhenPassportIdIsNull() throws Exception {
        AccountDto requestDto = AccountDto.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .profileId(TEST_PROFILE_ID)
                .money(TEST_SMALL_MONEY_AMOUNT)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .build();

        mockMvc.perform(post(TEST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(TEST_JSON_PATH_STATUS, is(TEST_STATUS_BAD_REQUEST)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERROR, is(TEST_ERROR_KEY_VALIDATION_FAILED)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERRORS_PASSPORT_ID, is(TEST_ERROR_KEY_PASSPORT_NULL)));
    }

    @Test
    @DisplayName("POST /accounts - 400 при валидации (profileId null)")
    void create_ShouldReturnBadRequest_WhenProfileIdIsNull() throws Exception {
        AccountDto requestDto = AccountDto.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .passportId(TEST_PASSPORT_ID)
                .money(TEST_SMALL_MONEY_AMOUNT)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .build();

        mockMvc.perform(post(TEST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(TEST_JSON_PATH_STATUS, is(TEST_STATUS_BAD_REQUEST)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERROR, is(TEST_ERROR_KEY_VALIDATION_FAILED)))
                .andExpect(jsonPath(TEST_JSON_PATH_ERRORS_PROFILE_ID, is(TEST_ERROR_KEY_PROFILE_NULL)));
    }

    private AccountDto buildAccountDto(Long id) {
        return AccountDto.builder()
                .id(id)
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .bankDetailsId(TEST_BANK_DETAILS_ID)
                .money(TEST_MONEY_AMOUNT)
                .negativeBalance(TEST_NEGATIVE_BALANCE)
                .passportId(TEST_PASSPORT_ID)
                .profileId(TEST_PROFILE_ID)
                .build();
    }
}

