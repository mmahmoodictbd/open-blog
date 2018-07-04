package com.unloadbrain.blog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

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

    // TODO: Remove map and put specific fields
    private Map<String, String> additionalProperties = new HashMap<>();

    private String googleAnalyticsAccountId;

}
