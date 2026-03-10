package com.bank.account.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.bank.account.util.ApplicationConstants.DEFAULT_SYSTEM_USER;

@Service
@Slf4j
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public String getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal())) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof String) {
                    return (String) principal;
                }
                return principal.toString();
            }
        } catch (Exception e) {
            log.warn("Could not extract current user from security context: {}", e.getMessage());
        }
        return DEFAULT_SYSTEM_USER;
    }
}

