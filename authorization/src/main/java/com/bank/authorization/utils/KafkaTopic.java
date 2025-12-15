package com.bank.authorization.utils;

import lombok.Getter;

@Getter
public enum KafkaTopic {
    USER_CREAT("user.create"),
    USER_DELETE("user.delete"),
    USER_UPDATE("user.update"),
    USER_GET("user.get"),
    USER_GET_REPLY("user.get.reply");

    private final String topicName;

    KafkaTopic(String topicName) {
        this.topicName = topicName;
    }

}
