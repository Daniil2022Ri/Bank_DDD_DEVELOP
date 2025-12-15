package com.bank.account.util;

import lombok.Getter;

@Getter
public enum OperationType {
    CREATE("CREATE"),
    UPDATE("UPDATE");

    private final String value;

    OperationType(String value) {
        this.value = value;
    }
}

