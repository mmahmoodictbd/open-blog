package com.unloadbrain.blog.exception;

public class InvalidPostStatusException extends RuntimeException {

    public InvalidPostStatusException(String message) {
        super(message);
    }
}
