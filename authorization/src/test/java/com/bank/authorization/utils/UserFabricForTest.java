package com.bank.authorization.utils;

import com.bank.authorization.dtos.events.UserCreatedEvent;
import com.bank.authorization.dtos.events.UserDeleteEvent;
import com.bank.authorization.dtos.events.UserUpdateEvent;
import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.dtos.user.UserDtoForUpdate;
import com.bank.authorization.entities.Role;
import com.bank.authorization.entities.User;

import static com.bank.authorization.utils.TestConstant.ENCODE_NEW_PASSWORD;
import static com.bank.authorization.utils.TestConstant.FIRST_USER_PROFILE_ID;
import static com.bank.authorization.utils.TestConstant.ID_FIRST_USER_FROM_DB;
import static com.bank.authorization.utils.TestConstant.ID_FOR_DELETE;
import static com.bank.authorization.utils.TestConstant.ID_FOR_UPDATE;
import static com.bank.authorization.utils.TestConstant.ID_SECOND_USER_FROM_DB;
import static com.bank.authorization.utils.TestConstant.NEW_PASSWORD;
import static com.bank.authorization.utils.TestConstant.OLD_PASSWORD;
import static com.bank.authorization.utils.TestConstant.SECOND_USER_PROFILE_ID;
import static com.bank.authorization.utils.TestConstant.UPDATED_PROFILE_ID;

public class UserFabricForTest {

    public static UserDto buildFirstUserDto() {
        return new UserDto(FIRST_USER_PROFILE_ID, OLD_PASSWORD, Role.ROLE_USER.name());
    }
    public static UserDto buildSecondUserDto() {
        return new UserDto(SECOND_USER_PROFILE_ID, OLD_PASSWORD, Role.ROLE_ADMIN.name());
    }
    public static UserCreatedEvent buildUserCreateEvent() {
        return new UserCreatedEvent(buildFirstUserDto());
    }
    public static UserUpdateEvent buildUserUpdateEvent() {
        return new UserUpdateEvent(buildUpdatedUserDto(), ID_FOR_UPDATE);
    }
    public static UserDtoForUpdate buildUpdatedUserDto() {
        return new UserDtoForUpdate(UPDATED_PROFILE_ID, NEW_PASSWORD, Role.ROLE_ADMIN.name());

    }
    public static UserDto buildExperedUpdatedUserDto() {
        return new UserDto(UPDATED_PROFILE_ID, NEW_PASSWORD, Role.ROLE_ADMIN.name());
    }
    public static User buildUpdatedUser() {
        return new User(ID_FIRST_USER_FROM_DB, UPDATED_PROFILE_ID, ENCODE_NEW_PASSWORD, Role.ROLE_ADMIN);
    }
    public static UserDeleteEvent buildUserDeleteEvent() {
        return new UserDeleteEvent(ID_FOR_DELETE);
    }

    public static User buildFirstUserFromDb() {
        return new User(ID_FIRST_USER_FROM_DB, FIRST_USER_PROFILE_ID, OLD_PASSWORD, Role.ROLE_USER);
    }
    public static User buildSecondUserFromDb() {
        return new User(ID_SECOND_USER_FROM_DB, SECOND_USER_PROFILE_ID, OLD_PASSWORD, Role.ROLE_ADMIN);
    }
    public static User buildFirstUserAfterMapping() {
        return new User(FIRST_USER_PROFILE_ID, ENCODE_NEW_PASSWORD, Role.ROLE_USER);
    }
    public static User buildUserForTestRepository(long profileId) {
        return new User(profileId, OLD_PASSWORD, Role.ROLE_USER);
    }
}
