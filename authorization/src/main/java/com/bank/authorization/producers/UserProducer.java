package com.bank.authorization.producers;

import com.bank.authorization.dtos.events.UserCreatedEvent;
import com.bank.authorization.dtos.events.UserDeleteEvent;
import com.bank.authorization.dtos.events.UserGetEvent;
import com.bank.authorization.dtos.events.UserUpdateEvent;
import com.bank.authorization.utils.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCreate(UserCreatedEvent userCreatedEvent) {
        sendMessage(KafkaTopic.USER_CREAT, userCreatedEvent);
    }

    public void sendDelete(UserDeleteEvent userDeleteEvent) {
        sendMessage(KafkaTopic.USER_DELETE, userDeleteEvent);
    }

    public void sendUpdate(UserUpdateEvent userUpdateEvent) {
        sendMessage(KafkaTopic.USER_UPDATE, userUpdateEvent);
    }

    public void sendGet(UserGetEvent userGetEvent) {
        sendMessage(KafkaTopic.USER_GET, userGetEvent);
    }


    private <T> void sendMessage(KafkaTopic kafkaTopic, T t) {
        kafkaTemplate.send(kafkaTopic.getTopicName(), t)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        RecordMetadata metadata = result.getRecordMetadata();
                        log.info("Сообщение отправлено в топик {}", metadata.topic());
                    } else {
                        log.error("Ошибка отправки сообщения в топик:{}", ex.getMessage(), ex);
                    }
                });
    }

}
