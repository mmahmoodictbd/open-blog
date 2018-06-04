package com.unloadbrain.blog.exception;

public class SiteNotFoundException extends RuntimeException {

    public SiteNotFoundException(String message) {
        super(message);
    }
}
