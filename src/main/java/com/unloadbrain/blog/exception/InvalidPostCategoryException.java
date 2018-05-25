package com.unloadbrain.blog.exception;

public class InvalidPostCategoryException extends RuntimeException {

    public InvalidPostCategoryException(String message) {
        super(message);
    }
}
