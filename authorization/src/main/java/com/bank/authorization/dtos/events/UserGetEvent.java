package com.bank.authorization.dtos.events;

import com.bank.authorization.utils.EventType;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetEvent extends UserEvent {
    @Positive(message = "id не может быть отрицательынм")
    private long userIdForSearch;

    public UserGetEvent(long userIdForSearch) {
        super(EventType.GET);
        this.userIdForSearch = userIdForSearch;
    }

    public UserGetEvent() {
        super(EventType.GET_ALL);
    }
}
