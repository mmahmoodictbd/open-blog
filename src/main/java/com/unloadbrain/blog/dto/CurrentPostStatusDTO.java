package com.unloadbrain.blog.dto;

import java.util.Arrays;

public enum CurrentPostStatusDTO {

    DRAFT, PUBLISHED;

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Unknown enum type %s, Allowed values are %s";

    public static CurrentPostStatusDTO fromValue(String value) {
        for (CurrentPostStatusDTO status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE_TEMPLATE, value, Arrays.toString(values())));
    }
}
