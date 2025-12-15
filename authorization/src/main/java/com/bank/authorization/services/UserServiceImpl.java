package com.bank.authorization.services;

import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.dtos.user.UserDtoForUpdate;
import com.bank.authorization.entities.User;
import com.bank.authorization.exceptions.CustomValidException;
import com.bank.authorization.mappers.UserMapper;
import com.bank.authorization.repositories.UserRepository;
import com.bank.authorization.utils.ApplicationConstants;
import io.micrometer.core.annotation.Timed;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.bank.authorization.utils.ApplicationConstants.EXISTS_PROFILE_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto findById(long id) {
        return userMapper.toDto(findUserById(id));
    }

    @Transactional
    @Override
    public UserDto update(UserDtoForUpdate updatedUser, long id) {
        User existUser = findUserById(id);
        if (updatedUser.getProfile() != null && userRepository
                .existsByProfileIdAndIdNot(updatedUser.getProfile(), id)) {
            throw new CustomValidException(EXISTS_PROFILE_ID);
        }
        if (updatedUser.getPassword() != null) {
            updatedUser.setPassword(
                    passwordEncoder.encode(updatedUser.getPassword())
            );
        }
        userMapper.updateUserFromDto(updatedUser, existUser);
        User actualUser = userRepository.save(existUser);
        log.info("Пользователь с id {} обновлен", id);
        return userMapper.toDto(actualUser);

    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.delete(findUserById(id));
        log.info("Пользователь с id {} удален", id);
    }

    @Transactional
    @Override
    @Timed
    public void save(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(
                userMapper.toEntity(user)
        );
        log.info("Пользователь {} успешно создан", user.getProfile());
    }

    private User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("Пользователь с id {} не сущесвует", id);
            return new EntityNotFoundException(String
                    .format("Пользователь с id %d не найден", id));
        });
    }
}
