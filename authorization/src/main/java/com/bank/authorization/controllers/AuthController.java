package com.bank.authorization.controllers;

import com.bank.authorization.dtos.auth.JwtResponse;
import com.bank.authorization.dtos.auth.LoginRequest;
import com.bank.authorization.services.CustomUserDetailsService;
import com.bank.authorization.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<?> getToken(@RequestBody LoginRequest loginRequest) {
        log.info("Обработка запроса аутентификации");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.error("Неверный логин или пароль", e);
            throw new BadCredentialsException("Логин или пароль не верный");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        log.info("Аутентификация успешно выполнена");
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
