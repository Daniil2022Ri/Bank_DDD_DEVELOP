package com.bank.account.service;

import com.bank.account.util.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrentUserServiceImpl implements CurrentUserService {

    private final ApplicationConstants applicationConstants;

    public CurrentUserServiceImpl(ApplicationConstants applicationConstants) {
        this.applicationConstants = applicationConstants;
    }

    @Override
    public String getCurrentUser() {
        return applicationConstants.DEFAULT_SYSTEM_USER;
    }
}

