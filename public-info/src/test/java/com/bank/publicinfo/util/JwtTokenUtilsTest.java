package com.bank.publicinfo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilsTest {

    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() {
        jwtTokenUtils = new JwtTokenUtils();
        ReflectionTestUtils.setField(jwtTokenUtils, "secret", "testSecretKeyForJWTTokenGeneration12345");
        ReflectionTestUtils.setField(jwtTokenUtils, "lifetime", 3600000L);
    }

    @Test
    void generateToken_ShouldCreateToken() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());

        String token = jwtTokenUtils.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void extractUsername_ShouldExtractUsername() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        String token = jwtTokenUtils.generateToken(userDetails);

        String username = jwtTokenUtils.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void isTokenExpired_WhenTokenIsNew_ShouldReturnFalse() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        String token = jwtTokenUtils.generateToken(userDetails);

        Boolean isExpired = jwtTokenUtils.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        String token = jwtTokenUtils.generateToken(userDetails);

        Boolean isValid = jwtTokenUtils.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidUsername() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        String token = jwtTokenUtils.generateToken(userDetails);
        
        UserDetails otherUser = new User("otheruser", "password", Collections.emptyList());

        Boolean isValid = jwtTokenUtils.validateToken(token, otherUser);

        assertFalse(isValid);
    }
}
