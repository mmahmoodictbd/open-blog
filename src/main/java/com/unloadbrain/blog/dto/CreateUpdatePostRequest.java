package com.unloadbrain.blog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

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

    private CurrentPostStatus status;

    private String permalink;

    private String featureImageLink;

    private Map<String, String> additionalProperties = new HashMap<>();

    @NotNull
    private PostActionDTO action;

}
