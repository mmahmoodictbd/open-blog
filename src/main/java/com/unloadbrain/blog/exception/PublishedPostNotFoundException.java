package com.unloadbrain.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such published post")
public class PublishedPostNotFoundException extends RuntimeException {

    public PublishedPostNotFoundException(String message) {
        super(message);
    }
}
