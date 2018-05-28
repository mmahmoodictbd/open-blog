package com.unloadbrain.blog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class PostDTO {

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

    private Date createdAt;

    private Date updatedAt;

}
