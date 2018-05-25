package com.unloadbrain.blog.exception;

public class DraftPostNotFoundException extends RuntimeException {

    public DraftPostNotFoundException(String message) {
        super(message);
    }
}
