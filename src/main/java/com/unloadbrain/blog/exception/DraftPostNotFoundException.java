package com.unloadbrain.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such draft post")
public class DraftPostNotFoundException extends RuntimeException {

    public DraftPostNotFoundException(String message) {
        super(message);
    }
}
