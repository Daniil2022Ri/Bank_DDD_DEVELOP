package com.bank.authorization.handlers;

import com.bank.authorization.exceptions.CustomValidException;
import com.bank.authorization.exceptions.UnknownEventException;
import com.bank.authorization.handlers.KafkaExceptionHandler;
import com.bank.authorization.utils.TestConstant;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.NetworkException;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.MessageListenerContainer;

import static com.bank.authorization.utils.TestConstant.ERROR_ENTITY_NOT_FOUND;
import static com.bank.authorization.utils.TestConstant.ERROR_HANDLER;
import static com.bank.authorization.utils.TestConstant.ERROR_NETWORK;
import static com.bank.authorization.utils.TestConstant.ERROR_TIMEOUT;
import static com.bank.authorization.utils.TestConstant.ERROR_UNKNOWN_EVENT;
import static com.bank.authorization.utils.TestConstant.MESSAGE_KEY;
import static com.bank.authorization.utils.TestConstant.MESSAGE_VALUE;
import static com.bank.authorization.utils.TestConstant.NAME_TOPIC;
import static com.bank.authorization.utils.TestConstant.OFFSET;
import static com.bank.authorization.utils.TestConstant.PARTITION;
import static com.bank.authorization.utils.TestConstant.VALIDATION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class KafkaExceptionHandlerTest {

    @Mock
    private Consumer<?, ?> consumer;
    @Mock
    private MessageListenerContainer listenerContainer;

    private ConsumerRecord<?, ?> record;

    @InjectMocks
    private KafkaExceptionHandler kafkaExceptionHandler;

    @BeforeEach
    void setUp() {
        record = new ConsumerRecord<>(
                NAME_TOPIC,
                PARTITION,
                OFFSET,
                MESSAGE_KEY,
                MESSAGE_VALUE
        );
    }

    @Test
    void handleRecordDeserializationException() {
        RecordDeserializationException exception = new RecordDeserializationException(
                new TopicPartition(record.topic(), record.partition()), record.offset(), ERROR_HANDLER, new RuntimeException(ERROR_HANDLER)
        );

        boolean result = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);

        assertThat(result).isTrue();
    }

    @Test
    void handleListenerExecutionFailedException_EntityNotFoundException() {
        EntityNotFoundException cause = new EntityNotFoundException(ERROR_ENTITY_NOT_FOUND);
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException(ERROR_HANDLER, cause);

        boolean result = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);

        assertThat(result).isTrue();
    }

    @Test
    void handleListenerExecutionFailedException_CustomValidException() {
        CustomValidException cause = new CustomValidException(VALIDATION_ERROR);
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException(ERROR_HANDLER, cause);

        boolean result = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);

        assertThat(result).isTrue();
    }

    @Test
    void handleListenerExecutionFailedException_UnknownEventException() {
        UnknownEventException cause = new UnknownEventException(ERROR_UNKNOWN_EVENT);
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException(ERROR_HANDLER, cause);

        boolean result = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);

        assertThat(result).isTrue();
    }

    @Test
    void handleRemainingException_TimeoutException() {
        TimeoutException exception = new TimeoutException(ERROR_TIMEOUT);

        boolean firstResult = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);
        assertThat(firstResult).isFalse();
        boolean secondResult = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);
        assertThat(secondResult).isFalse();
        boolean thirdResult = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);
        assertThat(thirdResult).isFalse();
        boolean lastResult = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);
        assertThat(lastResult).isTrue();

    }

    @Test
    void handleRemainingException_NetworkException() {
        NetworkException exception = new NetworkException(ERROR_NETWORK);

        boolean firstResult = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);
        assertThat(firstResult).isFalse();
        boolean secondResult = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);
        assertThat(secondResult).isFalse();
        boolean thirdResult = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);
        assertThat(thirdResult).isFalse();
        boolean lastResult = kafkaExceptionHandler.handleOne(exception, record, consumer, listenerContainer);
        assertThat(lastResult).isTrue();

    }
}