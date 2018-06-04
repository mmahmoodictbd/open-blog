package com.unloadbrain.blog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateSiteRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String siteUrl;

    @NotNull
    private String homeUrl;

}
