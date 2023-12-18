package com.cba.core.wirememobile.util;

public enum SettlementMethodEnum {
    MANUAL(1),
    AUTO(2);

    private final int value;

    SettlementMethodEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
