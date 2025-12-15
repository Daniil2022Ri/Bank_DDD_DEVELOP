package com.bank.authorization.utils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTopicManager {
    private AdminClient adminClient;
    private final KafkaProperties kafkaProperties;

    @PostConstruct
    public void init() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaProperties.getBootstrapServers());
        this.adminClient = AdminClient.create(config);
        buildTopic();
    }

    private void buildTopic() {
        for (KafkaTopic value : KafkaTopic.values()) {
            if (!topicExists(value.getTopicName())) {
                NewTopic topic = TopicBuilder.name(value.getTopicName()).build();
                try {
                    adminClient.createTopics(Collections.singleton(topic)).all().get();
                    log.info("Топик {} успешно создан", topic.name());
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Ошибка при создании топика {}", value.getTopicName(), e);
                }
            } else {
                log.debug("Топик {} уже существует", value.getTopicName());
            }
        }

    }

    private boolean topicExists(String topicName) {
        try {
            return adminClient.listTopics().names().get().contains(topicName);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при проверке существования топика {}", topicName, e);
            return false;
        }
    }

    @PreDestroy
    public void destroy() {
        if (adminClient != null) {
            adminClient.close();
        }
    }
}




