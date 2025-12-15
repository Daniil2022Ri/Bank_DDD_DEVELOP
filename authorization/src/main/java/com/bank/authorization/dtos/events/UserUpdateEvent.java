package com.bank.authorization.dtos.events;

import com.bank.authorization.dtos.user.UserDtoForUpdate;
import com.bank.authorization.utils.EventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateEvent extends UserEvent {
    @NotNull
    private long UserIdForUpdate;
    @Valid
    private UserDtoForUpdate data;

    public UserUpdateEvent() {
        super(EventType.UPDATE);
    }

    public UserUpdateEvent(UserDtoForUpdate data, long id) {
        super(EventType.UPDATE);
        this.data = data;
    }
}