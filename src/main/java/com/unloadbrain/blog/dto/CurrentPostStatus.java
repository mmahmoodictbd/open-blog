package com.unloadbrain.blog.dto;

import java.util.Arrays;

public enum CurrentPostStatus {

    DRAFT, PUBLISHED;

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Unknown enum type %s, Allowed values are %s";

    public static CurrentPostStatus fromValue(String value) {
        for (CurrentPostStatus status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE_TEMPLATE, value, Arrays.toString(values())));
    }
}
