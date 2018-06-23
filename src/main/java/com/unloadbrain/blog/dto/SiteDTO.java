package com.unloadbrain.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SiteDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String siteUrl;

    @NotNull
    private String homeUrl;

    private String keywords;
}
