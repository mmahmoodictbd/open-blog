package com.unloadbrain.blog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PostDTO {

    private long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private String category;

    private String tags;

    private String permalink;

    private String featureImageLink;

    @NotNull
    private String action;

}
