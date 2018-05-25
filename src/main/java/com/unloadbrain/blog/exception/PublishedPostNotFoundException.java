package com.unloadbrain.blog.exception;

public class PublishedPostNotFoundException extends RuntimeException {

    public PublishedPostNotFoundException(String message) {
        super(message);
    }
}
