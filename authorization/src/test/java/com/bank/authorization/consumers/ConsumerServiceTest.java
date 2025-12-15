package com.bank.authorization.consumers;

import com.bank.authorization.dtos.events.UserCreatedEvent;
import com.bank.authorization.dtos.events.UserDeleteEvent;
import com.bank.authorization.dtos.events.UserEvent;
import com.bank.authorization.dtos.events.UserGetEvent;
import com.bank.authorization.dtos.events.UserGetResponse;
import com.bank.authorization.dtos.events.UserUpdateEvent;
import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.entities.Role;
import com.bank.authorization.exceptions.UnknownEventException;
import com.bank.authorization.mappers.UserMapper;
import com.bank.authorization.services.UserService;
import com.bank.authorization.utils.EventType;
import com.bank.authorization.utils.UserFabricForTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static com.bank.authorization.utils.TestConstant.ERROR_UNKNOWN_EVENT;
import static com.bank.authorization.utils.TestConstant.FIRST_USER_PROFILE_ID;
import static com.bank.authorization.utils.TestConstant.ID_FOR_DELETE;
import static com.bank.authorization.utils.TestConstant.OLD_PASSWORD;
import static com.bank.authorization.utils.TestConstant.SEARCH_ID;
import static com.bank.authorization.utils.TestConstant.STATUS_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private ConsumerService consumerService;

    @Test
    void create() {
        UserDto user = UserFabricForTest.buildFirstUserDto();
        UserCreatedEvent createdEvent = UserFabricForTest.buildUserCreateEvent();

        consumerService.create(createdEvent);

        verify(userService).save(argThat(userDto ->
                userDto.getProfile() == FIRST_USER_PROFILE_ID &&
                userDto.getPassword().equals(OLD_PASSWORD) &&
                userDto.getRole().equals(Role.ROLE_USER.name())));
        assertThat(createdEvent.getAction()).isEqualTo(EventType.CREAT);
    }

    @Test
    void update() {
        UserUpdateEvent updateEvent = UserFabricForTest.buildUserUpdateEvent();

        consumerService.update(updateEvent);

        verify(userService).update(updateEvent.getData(), updateEvent.getUserIdForUpdate());
        assertThat(updateEvent.getAction()).isEqualTo(EventType.UPDATE);
    }

    @Test
    void delete() {
        UserDeleteEvent deleteEvent = UserFabricForTest.buildUserDeleteEvent();


        consumerService.delete(deleteEvent);

        verify(userService).delete(ID_FOR_DELETE);
        assertThat(deleteEvent.getAction()).isEqualTo(EventType.DELETE);
    }

    @Test
    void get_all() {
        UserDto user1 = UserFabricForTest.buildFirstUserDto();
        UserDto user2 = UserFabricForTest.buildSecondUserDto();
        List<UserDto> expectedUSers = List.of(user1, user2);

        doReturn(expectedUSers).when(userService).findAll();

        UserGetResponse response = consumerService.get(new UserGetEvent());

        verify(userService, never()).findById(anyLong());
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getStatus()).isEqualTo(STATUS_OK);
        assertThat(response.getData()).isEqualTo(List.of(user1, user2));
    }

    @Test
    void get_byId() {
        UserDto user = UserFabricForTest.buildFirstUserDto();

        doReturn(user).when(userService).findById(anyLong());


        UserGetResponse response = consumerService.get(new UserGetEvent(SEARCH_ID));

        verify(userService).findById(SEARCH_ID);
        verify(userService, never()).findAll();
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getStatus()).isEqualTo(STATUS_OK);
        assertThat(response.getData()).isEqualTo(List.of(user));
    }

    @ParameterizedTest
    @MethodSource("setArg")
    void unknowEvent(UserEvent userEvent) {
        userEvent.setAction(EventType.UNKNOWN);
        switch (userEvent.getAction()) {
            case CREAT:
                assertThatThrownBy(() ->
                        consumerService.create((UserCreatedEvent) userEvent))
                        .isInstanceOf(UnknownEventException.class).hasMessage(ERROR_UNKNOWN_EVENT);
                break;
            case UPDATE:
                assertThatThrownBy(() ->
                        consumerService.update((UserUpdateEvent) userEvent))
                        .isInstanceOf(UnknownEventException.class).hasMessage(ERROR_UNKNOWN_EVENT);
                break;
            case DELETE:
                assertThatThrownBy(() ->
                        consumerService.delete((UserDeleteEvent) userEvent))
                        .isInstanceOf(UnknownEventException.class).hasMessage(ERROR_UNKNOWN_EVENT);
                break;
            case GET_ALL:
                assertThatThrownBy(() ->
                        consumerService.get((UserGetEvent) userEvent))
                        .isInstanceOf(UnknownEventException.class).hasMessage(ERROR_UNKNOWN_EVENT);
                break;
        }
    }

    static Stream<Arguments> setArg() {
        return Stream.of(
                Arguments.of(UserFabricForTest.buildUserCreateEvent()),
                Arguments.of(UserFabricForTest.buildUserUpdateEvent()),
                Arguments.of(UserFabricForTest.buildUserDeleteEvent()),
                Arguments.of(new UserGetEvent())
        );
    }


}