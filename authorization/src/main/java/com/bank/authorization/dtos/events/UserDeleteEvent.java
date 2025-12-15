package com.bank.authorization.dtos.events;

import com.bank.authorization.utils.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteEvent extends UserEvent{

    @NotNull(message = "Укажите id пользователя")
    private long userIdForDelete;

    public UserDeleteEvent() {
        super(EventType.DELETE);
    }

    public UserDeleteEvent(long userIdForDelete) {
        super(EventType.DELETE);
        this.userIdForDelete = userIdForDelete;
    }
}
