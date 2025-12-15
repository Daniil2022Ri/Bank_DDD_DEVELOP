package com.bank.authorization.config;

import com.bank.authorization.dtos.events.UserCreatedEvent;
import com.bank.authorization.dtos.events.UserDeleteEvent;
import com.bank.authorization.dtos.events.UserGetEvent;
import com.bank.authorization.dtos.events.UserUpdateEvent;
import com.bank.authorization.handlers.KafkaExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ConsumerConfig implements KafkaListenerConfigurer {

    @Value("${kafka.json.trusted-packages:com.bank.authorization.dtos.events}")
    private String trustedPackages;
    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaExceptionHandler kafkaExceptionHandler;
    private final Validator validator;


    private Map<String, Object> defaultConsumerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        return config;
    }

    @Bean
    public ConsumerFactory<String, UserCreatedEvent> consumerFactoryForCreate() {
        JsonDeserializer<UserCreatedEvent> deserializer = new JsonDeserializer<>(UserCreatedEvent.class, false);
        deserializer.addTrustedPackages(trustedPackages);
        return new DefaultKafkaConsumerFactory<>(defaultConsumerConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> containerFactoryForCreate() {

        ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactoryForCreate());
        containerFactory.setReplyTemplate(kafkaTemplate);
        containerFactory.setCommonErrorHandler(kafkaExceptionHandler);
        containerFactory.getContainerProperties().setMissingTopicsFatal(false);
        return containerFactory;
    }

    @Bean
    public ConsumerFactory<String, UserUpdateEvent> consumerFactoryForUpdate() {
        JsonDeserializer<UserUpdateEvent> deserializer = new JsonDeserializer<>(UserUpdateEvent.class, false);
        deserializer.addTrustedPackages(trustedPackages);
        return new DefaultKafkaConsumerFactory<>(defaultConsumerConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserUpdateEvent> containerFactoryForUpdate() {

        ConcurrentKafkaListenerContainerFactory<String, UserUpdateEvent> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactoryForUpdate());
        containerFactory.setReplyTemplate(kafkaTemplate);
        containerFactory.setCommonErrorHandler(kafkaExceptionHandler);
        containerFactory.getContainerProperties().setMissingTopicsFatal(false);
        return containerFactory;
    }

    @Bean
    public ConsumerFactory<String, UserDeleteEvent> consumerFactoryForDelete() {
        JsonDeserializer<UserDeleteEvent> deserializer = new JsonDeserializer<>(UserDeleteEvent.class, false);
        deserializer.addTrustedPackages(trustedPackages);
        return new DefaultKafkaConsumerFactory<>(defaultConsumerConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDeleteEvent> containerFactoryForDelete() {

        ConcurrentKafkaListenerContainerFactory<String, UserDeleteEvent> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactoryForDelete());
        containerFactory.setReplyTemplate(kafkaTemplate);
        containerFactory.setCommonErrorHandler(kafkaExceptionHandler);
        containerFactory.getContainerProperties().setMissingTopicsFatal(false);
        return containerFactory;
    }

    @Bean
    public ConsumerFactory<String, UserGetEvent> consumerFactoryForGet() {
        JsonDeserializer<UserGetEvent> deserializer = new JsonDeserializer<>(UserGetEvent.class, false);
        deserializer.addTrustedPackages(trustedPackages);
        return new DefaultKafkaConsumerFactory<>(defaultConsumerConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserGetEvent> containerFactoryForGet() {

        ConcurrentKafkaListenerContainerFactory<String, UserGetEvent> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactoryForGet());
        containerFactory.setReplyTemplate(kafkaTemplate);
        containerFactory.setCommonErrorHandler(kafkaExceptionHandler);
        containerFactory.getContainerProperties().setMissingTopicsFatal(false);
        return containerFactory;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(validator);
    }
}
