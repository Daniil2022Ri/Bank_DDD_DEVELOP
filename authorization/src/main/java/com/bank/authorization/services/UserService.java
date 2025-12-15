package com.bank.authorization.services;

import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.dtos.user.UserDtoForUpdate;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto findById(long id);
    UserDto update(UserDtoForUpdate updatedUser, long id);
    void delete(long id);
    void save(UserDto user);
}
