package com.bank.authorization.services;


import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.dtos.user.UserDtoForUpdate;
import com.bank.authorization.entities.Role;
import com.bank.authorization.entities.User;
import com.bank.authorization.exceptions.CustomValidException;
import com.bank.authorization.mappers.UserMapper;
import com.bank.authorization.repositories.UserRepository;
import com.bank.authorization.utils.UserFabricForTest;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.bank.authorization.utils.TestConstant.ENCODE_NEW_PASSWORD;
import static com.bank.authorization.utils.TestConstant.ERROR_ENTITY_NOT_FOUND;
import static com.bank.authorization.utils.TestConstant.ERROR_EXISTS_PROFILE_ID;
import static com.bank.authorization.utils.TestConstant.FIRST_USER_PROFILE_ID;
import static com.bank.authorization.utils.TestConstant.ID_FOR_DELETE;
import static com.bank.authorization.utils.TestConstant.ID_FOR_UPDATE;
import static com.bank.authorization.utils.TestConstant.NEW_PASSWORD;
import static com.bank.authorization.utils.TestConstant.OLD_PASSWORD;
import static com.bank.authorization.utils.TestConstant.SEARCH_ID;
import static com.bank.authorization.utils.TestConstant.UPDATED_PROFILE_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void getAllUsers_notEmpty() {
        UserDto userDto1 = UserFabricForTest.buildFirstUserDto();
        UserDto userDto2 = UserFabricForTest.buildSecondUserDto();
        List<UserDto> expectedUsers = List.of(userDto1, userDto2);
        User user1 = UserFabricForTest.buildFirstUserFromDb();
        User user2 = UserFabricForTest.buildSecondUserFromDb();
        List<User> userFromTheDb = List.of(user1, user2);

        doReturn(userFromTheDb).when(userRepository).findAll();
        doReturn(userDto1).when(userMapper).toDto(user1);
        doReturn(userDto2).when(userMapper).toDto(user2);

        List<UserDto> actualUsers = userService.findAll();

        verify(userMapper).toDto(user1);
        verify(userMapper).toDto(user2);
        Assertions.assertThat(actualUsers).isEqualTo(expectedUsers);
        Assertions.assertThat(actualUsers).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_empty() {
        doReturn(Collections.emptyList()).when(userRepository).findAll();
        List<UserDto> actualUsers = userService.findAll();

        Assertions.assertThat(actualUsers).isEqualTo(Collections.emptyList());
        Assertions.assertThat(actualUsers).isEmpty();
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void getUserById_exist() {
        UserDto expectedUser = UserFabricForTest.buildFirstUserDto();
        User userFromDb = UserFabricForTest.buildFirstUserFromDb();
        doReturn(Optional.of(userFromDb)).when(userRepository).findById(SEARCH_ID);
        doReturn(expectedUser).when(userMapper).toDto(userFromDb);

        UserDto actualUser = userService.findById(SEARCH_ID);

        Assertions.assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void getUserById_notExist() {
        doReturn(Optional.empty()).when(userRepository).findById(SEARCH_ID);
        Assertions.assertThatThrownBy(() -> userService.findById(SEARCH_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ERROR_ENTITY_NOT_FOUND);
        verify(userRepository, times(1)).findById(SEARCH_ID);
        verify(userMapper , never()).toDto(any());
    }

    @Test
    void update_newProfileIdNotExist() {
        UserDtoForUpdate updatedUser = UserFabricForTest.buildUpdatedUserDto();
        User updatedUserForDb = UserFabricForTest.buildUpdatedUser();
        User existUser = UserFabricForTest.buildFirstUserFromDb();
        UserDto experedUserDto = UserFabricForTest.buildExperedUpdatedUserDto();

        doReturn(Optional.of(existUser)).when(userRepository).findById(ID_FOR_UPDATE);
        doReturn(false).when(userRepository).existsByProfileIdAndIdNot(anyLong(), anyLong());
        doReturn(ENCODE_NEW_PASSWORD).when(passwordEncoder).encode(anyString());
        doReturn(updatedUserForDb).when(userRepository).save(any(User.class));
        doReturn(experedUserDto).when(userMapper).toDto(any(User.class));

        UserDto actualUser = userService.update(updatedUser, ID_FOR_UPDATE);

        Assertions.assertThat(actualUser).isEqualTo(experedUserDto);

        verify(userMapper).updateUserFromDto(updatedUser, existUser);
        verify(userMapper).toDto(updatedUserForDb);
        verify(passwordEncoder).encode(NEW_PASSWORD);
        verify(userRepository).existsByProfileIdAndIdNot(UPDATED_PROFILE_ID, ID_FOR_UPDATE);
        verify(userRepository).save(existUser);

    }

    @Test
    void update_newProfileIdExistAnotherUser() {
        UserDtoForUpdate updatedUser = UserFabricForTest.buildUpdatedUserDto();

        doReturn(Optional.of(UserFabricForTest.buildFirstUserFromDb()))
                .when(userRepository).findById(ID_FOR_UPDATE);
        doReturn(true).when(userRepository).existsByProfileIdAndIdNot(UPDATED_PROFILE_ID, ID_FOR_UPDATE);

        Assertions.assertThatThrownBy(() -> userService.update(updatedUser, 1))
                .isInstanceOf(CustomValidException.class)
                .hasMessage(ERROR_EXISTS_PROFILE_ID);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void update_oldProfileId() {
        UserDtoForUpdate updatedUser = UserFabricForTest.buildUpdatedUserDto();
        updatedUser.setProfile(null);
        User existUser = UserFabricForTest.buildFirstUserFromDb();
        UserDto expectedUser = UserFabricForTest.buildExperedUpdatedUserDto();
        expectedUser.setProfile(FIRST_USER_PROFILE_ID);

        doReturn(Optional.of(existUser)).when(userRepository).findById(ID_FOR_UPDATE);
        doReturn(ENCODE_NEW_PASSWORD).when(passwordEncoder).encode(NEW_PASSWORD);
        doReturn(existUser).when(userRepository).save(any(User.class));
        doReturn(expectedUser).when(userMapper).toDto(any(User.class));

        UserDto actualUser = userService.update(updatedUser, ID_FOR_UPDATE);

        Assertions.assertThat(actualUser).isEqualTo(expectedUser);
        verify(userMapper).updateUserFromDto(updatedUser, existUser);
        verify(passwordEncoder).encode(NEW_PASSWORD);
        verify(userRepository).save(existUser);

    }

    @Test
    void update_notExistUser() {
        UserDtoForUpdate updatedUser = UserFabricForTest.buildUpdatedUserDto();

        doReturn(Optional.empty()).when(userRepository).findById(ID_FOR_UPDATE);
        Assertions.assertThatThrownBy(() -> userService.update(updatedUser, ID_FOR_UPDATE)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ERROR_ENTITY_NOT_FOUND);
        verifyNoInteractions(passwordEncoder);
    }


    @Test
    void delete_notExistUser() {
        doReturn(Optional.empty()).when(userRepository).findById(ID_FOR_DELETE);

        Assertions.assertThatThrownBy(() -> userService.delete(ID_FOR_DELETE)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ERROR_ENTITY_NOT_FOUND);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void delete_existUser() {
        User existUser = UserFabricForTest.buildFirstUserFromDb();

        doReturn(Optional.of(existUser)).when(userRepository).findById(ID_FOR_DELETE);

        userService.delete(ID_FOR_DELETE);
        verify(userRepository).delete(existUser);
    }


    @Test
    void createNewUser() {
        UserDto newUser = UserFabricForTest.buildFirstUserDto();
        User userAfterMapping = UserFabricForTest.buildFirstUserAfterMapping();

        doReturn(ENCODE_NEW_PASSWORD).when(passwordEncoder).encode(OLD_PASSWORD);
        doReturn(userAfterMapping).when(userMapper).toEntity(newUser);

        userService.save(newUser);

        verify(passwordEncoder).encode(OLD_PASSWORD);
        verify(userRepository).save(userAfterMapping);
        verify(userRepository).save(argThat(user -> ENCODE_NEW_PASSWORD.equals(user.getPassword())));
    }

}