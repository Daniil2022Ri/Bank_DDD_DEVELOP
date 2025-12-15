package com.bank.authorization.dtos.events;


import com.bank.authorization.utils.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public abstract class UserEvent {
    private String requestId;
    @NotNull(message = "Не может быть пустым")
    private EventType action;

    public UserEvent(EventType action) {
        requestId = UUID.randomUUID().toString();
        this.action = action;
    }
}
