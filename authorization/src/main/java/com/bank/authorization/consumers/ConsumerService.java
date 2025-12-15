package com.bank.authorization.consumers;

import com.bank.authorization.dtos.events.UserCreatedEvent;
import com.bank.authorization.dtos.events.UserDeleteEvent;
import com.bank.authorization.dtos.events.UserEvent;
import com.bank.authorization.dtos.events.UserGetEvent;
import com.bank.authorization.dtos.events.UserGetResponse;
import com.bank.authorization.dtos.events.UserUpdateEvent;
import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.exceptions.UnknownEventException;
import com.bank.authorization.services.UserService;
import com.bank.authorization.utils.EventType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bank.authorization.utils.ApplicationConstants.STATUS_ERROR;
import static com.bank.authorization.utils.ApplicationConstants.STATUS_OK;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {
    private final UserService userService;

    @KafkaListener(topics = "user.create", containerFactory = "containerFactoryForCreate")
    public void create(@Valid UserCreatedEvent userCreatedEvent) {
        log.info("Сообщение из топика user.create в обработке");
        chekAction(userCreatedEvent);
        userService.save(userCreatedEvent.getData());
        log.info("Обработка сообщение из топика user.create успешно выполнено");
    }

    @KafkaListener(topics = "user.update", containerFactory = "containerFactoryForUpdate")
    public void update(@Valid UserUpdateEvent userUpdateEvent) {
        log.info("Сообщение из топика user.update в обработке");
        chekAction(userUpdateEvent);
        userService.update(
                userUpdateEvent.getData(),
                userUpdateEvent.getUserIdForUpdate()
        );
        log.info("Обработка сообщение из топика user.update успешно выполнено");
    }

    @KafkaListener(topics = "user.delete", containerFactory = "containerFactoryForDelete")
    public void delete(@Valid UserDeleteEvent userDeleteEvent) {
        log.info("Сообщение из топика user.delete в обработке");
        chekAction(userDeleteEvent);
        userService.delete(userDeleteEvent.getUserIdForDelete());
        log.info("Обработка сообщение из топика user.delete успешно выполнено");
    }

    @KafkaListener(topics = "user.get", containerFactory = "containerFactoryForGet")
    @SendTo("user.get.reply")
    public UserGetResponse get(UserGetEvent userGetEvent) {
        log.info("Сообщение из топика user.get в обработке");
        chekAction(userGetEvent);
        switch (userGetEvent.getAction()) {
            case GET:
                UserDto user = userService.findById(userGetEvent.getUserIdForSearch());
                log.info("Отправление запрошенного пользователя");
                return new UserGetResponse(userGetEvent.getRequestId(), STATUS_OK,
                        Collections.singletonList(user));
            case GET_ALL:
                List<UserDto> users = userService.findAll();
                log.info("Отправление запрошенных пользователей");
                return new UserGetResponse(userGetEvent.getRequestId(), STATUS_OK, users);
            default:
                log.warn("Неизвестный запрос");
                return new UserGetResponse(userGetEvent.getRequestId(), STATUS_ERROR, Collections.emptyList());
        }
    }

    private void chekAction(UserEvent userEvent) {
        List<String> events = Arrays.stream(EventType.values()).map(Enum::name).toList();
        if (!events.contains(userEvent.getAction().name())) {
            log.error("Неизвестное событие {}", userEvent.getAction());
            throw new UnknownEventException("Неизвестнное событие");
        }
    }
}
