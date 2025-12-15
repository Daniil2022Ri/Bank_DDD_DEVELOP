package com.bank.authorization.producers;

import com.bank.authorization.dtos.events.UserCreatedEvent;
import com.bank.authorization.dtos.events.UserDeleteEvent;
import com.bank.authorization.dtos.events.UserEvent;
import com.bank.authorization.dtos.events.UserGetEvent;
import com.bank.authorization.dtos.events.UserUpdateEvent;
import com.bank.authorization.utils.KafkaTopic;
import com.bank.authorization.utils.UserFabricForTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.bank.authorization.utils.TestConstant.SEARCH_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProducerTest {
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks
    private UserProducer userProducer;

    @ParameterizedTest
    @MethodSource("setArgs")
    void sendCreate(UserEvent event, KafkaTopic topic) {
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(anyString(), any(UserEvent.class))).thenReturn(future);
        switch (event.getAction()) {
            case CREAT:
                userProducer.sendCreate((UserCreatedEvent) event);
                break;
            case UPDATE:
                userProducer.sendUpdate((UserUpdateEvent) event);
                break;
            case DELETE:
                userProducer.sendDelete((UserDeleteEvent) event);
                break;
            case GET_ALL, GET:
                userProducer.sendGet((UserGetEvent) event);
                break;
        }
        verify(kafkaTemplate).send(eq(topic.getTopicName()), eq(event));
    }


    static Stream<Arguments> setArgs() {
        return Stream.of(
                Arguments.of(
                        UserFabricForTest.buildUserCreateEvent(),
                        KafkaTopic.USER_CREAT
                ),
                Arguments.of(
                        UserFabricForTest.buildUserUpdateEvent(),
                        KafkaTopic.USER_UPDATE
                ),
                Arguments.of(
                        UserFabricForTest.buildUserDeleteEvent(), KafkaTopic.USER_DELETE
                ),
                Arguments.of(
                        new UserGetEvent(), KafkaTopic.USER_GET
                ),
                Arguments.of(
                        new UserGetEvent(SEARCH_ID), KafkaTopic.USER_GET
                )
        );
    }
}