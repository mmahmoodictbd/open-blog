package com.unloadbrain.blog.converter;

import com.unloadbrain.blog.dto.CurrentPostStatus;

import java.beans.PropertyEditorSupport;

public class PostStatusConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(CurrentPostStatus.fromValue(text));
    }

}