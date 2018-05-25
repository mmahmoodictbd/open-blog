package com.unloadbrain.blog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateUpdatePostRequest {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private String summary;

    private String categories;

    private String tags;

    private CurrentPostStatusDTO status;

    private String permalink;

    private String featureImageLink;

    @NotNull
    private PostActionDTO action;

}
