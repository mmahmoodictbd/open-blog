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
public class SiteQueryDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String siteUrl;

    @NotNull
    private String homeUrl;

    private String metaKeywords;

    private String socialLinkedInUrl;

    private String socialGithubUrl;

    private String socialFBUrl;

    private String socialTwitterUrl;

    private String socialGooglePlusUrl;

    private String socialWikiPageUrl;

    private String socialEmailMe;

    private String googleAnalyticsAccountId;

    private String googleSiteVerificationId;

    private String bingSiteVerificationId;
}
