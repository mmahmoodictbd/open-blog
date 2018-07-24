package com.unloadbrain.blog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PostQueryDTO {

    private Long id;

    private String title;

    private String content;

    private String summary;

    private String categories;

    private String tags;

    private CurrentPostStatus status;

    private String permalink;

    private String featureImageLink;

    private String seoTitle;

    private String seoSlug;

    private String seoDescription;

    private String seoTags;

    private Date createdAt;

    private Date updatedAt;

}
