package com.bank.authorization.handlers;

import com.bank.authorization.exceptions.CustomValidException;
import com.bank.authorization.exceptions.UnknownEventException;
import com.bank.authorization.utils.ApplicationConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.NetworkException;
import org.apache.kafka.common.errors.NotLeaderOrFollowerException;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.bank.authorization.utils.ApplicationConstants.*;

@Component
@Slf4j
public class KafkaExceptionHandler implements CommonErrorHandler {

    private int retryCount = 0; //лучше мапу для параллельной работы

    @Override
    public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record,
                             Consumer<?, ?> consumer, MessageListenerContainer container) {

        if (thrownException instanceof ListenerExecutionFailedException) {
            return handleListenerExecutionFailedException(
                    (ListenerExecutionFailedException) thrownException, record);
        }
        return handleRemainingException(thrownException, record);
    }

    private boolean handleListenerExecutionFailedException(ListenerExecutionFailedException e,
                                                           ConsumerRecord<?, ?> record) {
        Throwable exception = getRootCause(e);

        if (exception instanceof MethodArgumentNotValidException) {
            return handleMethodArgumentNotValidException((MethodArgumentNotValidException) exception,
                    record);
        } else if (exception instanceof EntityNotFoundException) {
            return handleEntityNotFoundException((EntityNotFoundException) exception, record);
        } else if (exception instanceof CustomValidException) {
            return handleCustomValidException((CustomValidException) exception, record);
        } else if (exception instanceof UnknownEventException) {
            return handleUnknownEventException((UnknownEventException) exception, record);
        } else if (isTemporary(exception)) {
            return handleTemporaryException(exception, record);
        }
        log.error(ERROR_KAFKA_HANDLER, record.topic(), exception.getMessage(), exception);
        return true;
    }

    private boolean handleEntityNotFoundException(EntityNotFoundException e, ConsumerRecord<?, ?> record) {
        log.error(ERROR_KAFKA_HANDLER, record.topic(), e.getMessage(), e);
        return true;
    }

    private boolean handleUnknownEventException(UnknownEventException e, ConsumerRecord<?, ?> record) {
        log.error(ERROR_KAFKA_HANDLER, record.topic(), e.getMessage(), e);
        return true;
    }

    private boolean handleMethodArgumentNotValidException(MethodArgumentNotValidException e, ConsumerRecord<?, ?> record) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format(PATTERN_FOR_FIELD_ERROR, error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(FOR_JOINING));
        log.error("Ошибка валидации. {}", errorMessage, e);
        return true;
    }

    private boolean handleCustomValidException(CustomValidException e, ConsumerRecord<?, ?> record) {
        log.error(ERROR_KAFKA_HANDLER, record.topic(), e.getMessage(), e);
        return true;
    }

    private boolean handleRemainingException(Exception e, ConsumerRecord<?, ?> record) {
        if (isTemporary(e)) {
            return handleTemporaryException(e, record);
        }
        log.error(ERROR_KAFKA_HANDLER, record.topic(), e.getMessage(), e);
        return true;
    }

    private boolean handleTemporaryException(Throwable e, ConsumerRecord<?, ?> record) {
        retryCount++;
        log.warn("Временная ошибка {}", e.getMessage());
        if (retryCount <= MAX_RETRY) {
            log.warn("Попытка {}/{} обработки сообщения из топика {}, после временной ошибки {}",
                    retryCount, MAX_RETRY, record.topic(), e.getMessage());
            return false;
        } else {
            log.error(ERROR_KAFKA_HANDLER, record.topic(), e.getMessage(), e);
            retryCount = 0;
            return true;
        }

    }

    private boolean isTemporary(Throwable e) {
        return e instanceof NetworkException ||
                e instanceof TransientDataAccessException ||
                e instanceof TimeoutException ||
                e instanceof JDBCConnectionException ||
                e instanceof NotLeaderOrFollowerException;
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    @Override
    public void handleOtherException(Exception thrownException, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        if (thrownException instanceof RecordDeserializationException) {
            RecordDeserializationException exp = (RecordDeserializationException) thrownException;
            log.warn("Ошибка при десериализации сообщения из топика {}", container.getContainerProperties().getTopics(), thrownException);
            consumer.seek(exp.topicPartition(), exp.offset() + 1);
        }
    }


}
