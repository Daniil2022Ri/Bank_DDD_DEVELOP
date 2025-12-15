package com.bank.authorization.dtos.events;

import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.utils.EventType;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserCreatedEvent extends UserEvent{

    @Valid
    private UserDto data;

    public UserCreatedEvent() {
        super(EventType.CREAT);
    }

    public UserCreatedEvent(UserDto data) {
        super(EventType.CREAT);
        this.data = data;
    }
}
