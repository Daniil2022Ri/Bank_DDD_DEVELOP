package com.bank.authorization.controllers;

import com.bank.authorization.dtos.auth.LoginRequest;
import com.bank.authorization.entities.Role;
import com.bank.authorization.services.CustomUserDetailsService;
import com.bank.authorization.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static com.bank.authorization.utils.TestConstant.ENDPOINT_AUTH;
import static com.bank.authorization.utils.TestConstant.ERROR_BAD_CREDENTIALS;
import static com.bank.authorization.utils.TestConstant.HEADER_VALID_TOKEN;
import static com.bank.authorization.utils.TestConstant.JSON_PATH_MESSAGE;
import static com.bank.authorization.utils.TestConstant.JSON_PATH_TOKEN;
import static com.bank.authorization.utils.TestConstant.LOGIN_PASSWORD;
import static com.bank.authorization.utils.TestConstant.LOGIN_USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenUtils jwtTokenUtils;


    @Test
    void getToken_validCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest(
                LOGIN_USERNAME, LOGIN_PASSWORD
        );
        String loginJson = objectMapper.writeValueAsString(loginRequest);
        UserDetails userDetails = new User(loginRequest.getUsername(), loginRequest.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name())));
        UsernamePasswordAuthenticationToken validAuthToken =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        String token = HEADER_VALID_TOKEN;

        doReturn(validAuthToken).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        doReturn(userDetails).when(userDetailsService).loadUserByUsername(anyString());
        doReturn(token).when(jwtTokenUtils).generateToken(any(UserDetails.class));


        mockMvc.perform(post(ENDPOINT_AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TOKEN).value(token));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(loginRequest.getUsername());
        verify(jwtTokenUtils).generateToken(userDetails);
    }

    @Test
    void getToken_badCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest(
                LOGIN_USERNAME, LOGIN_PASSWORD
        );
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        doThrow(BadCredentialsException.class)
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(post(ENDPOINT_AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath(JSON_PATH_MESSAGE).value(ERROR_BAD_CREDENTIALS));
    }
}