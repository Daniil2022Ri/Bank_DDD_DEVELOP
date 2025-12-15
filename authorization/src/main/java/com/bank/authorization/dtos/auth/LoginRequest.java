package com.bank.authorization.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
