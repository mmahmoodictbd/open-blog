package com.unloadbrain.blog.converter;

import com.unloadbrain.blog.dto.PostStatusDTO;

import java.beans.PropertyEditorSupport;

public class PostStatusConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(PostStatusDTO.fromValue(text));
    }

}