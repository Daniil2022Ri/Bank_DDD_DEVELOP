package com.bank.authorization.services;

import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.entities.User;

public interface AuditService {
    void addCreate(UserDto user);
    void addUpdate(UserDto oldUser, UserDto newUser);
}
