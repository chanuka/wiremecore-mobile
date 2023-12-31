package com.cba.core.wirememobile.util;

public enum DeviceTypeEnum {
    WEB(1),
    MPOS(2),
    EDC_POS(3);

    private final int value;

    DeviceTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
