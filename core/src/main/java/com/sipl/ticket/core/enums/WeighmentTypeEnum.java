package com.sipl.ticket.core.enums;

public enum WeighmentTypeEnum {
    SOURCE(1, "Source"),
    DESTINATION(2, "Destination");

    private final int code;
    private final String description;

    WeighmentTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static WeighmentTypeEnum fromCode(int code) {
        for (WeighmentTypeEnum type : WeighmentTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid WeighmentTypeEnum code: " + code);
    }
}

