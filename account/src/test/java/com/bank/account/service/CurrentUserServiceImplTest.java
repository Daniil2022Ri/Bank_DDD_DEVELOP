package com.bank.account.service;

import com.bank.account.util.ApplicationConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentUserServiceImplTest {

    private static final String TEST_SYSTEM_USER = "system";

    @Mock
    private ApplicationConstants applicationConstants;

    @InjectMocks
    private CurrentUserServiceImpl currentUserService;

    @Test
    void getCurrentUser_ShouldReturnSystemUser() {
        when(applicationConstants.DEFAULT_SYSTEM_USER).thenReturn(TEST_SYSTEM_USER);

        String result = currentUserService.getCurrentUser();

        assertEquals(TEST_SYSTEM_USER, result);
    }
}

