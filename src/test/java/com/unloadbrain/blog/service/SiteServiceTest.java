package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.model.Site;
import com.unloadbrain.blog.domain.repository.SiteRepository;
import com.unloadbrain.blog.dto.SiteQueryDTO;
import com.unloadbrain.blog.dto.SiteUpdateCommandDTO;
import com.unloadbrain.blog.exception.SiteNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SiteServiceTest {

    private static final String META_KEYWORDS = "metaKeywords";
    private static final String SOCIAL_LINKED_IN_URL = "socialLinkedInUrl";
    private static final String SOCIAL_GITHUB_URL = "socialGithubUrl";
    private static final String SOCIAL_FB_URL = "socialFBUrl";
    private static final String SOCIAL_TWITTER_URL = "socialTwitterUrl";
    private static final String SOCIAL_GOOGLE_PLUS_URL = "socialGooglePlusUrl";
    private static final String SOCIAL_WIKI_PAGE_URL = "socialWikiPageUrl";
    private static final String SOCIAL_EMAIL_ME = "socialEmailMe";
    private static final String GOOGLE_ANALYTICS_ACCOUNT_ID = "googleAnalyticsAccountId";
    private static final String GOOGLE_SITE_VERIFICATION_ID = "googleSiteVerificationId";
    private static final String BING_SITE_VERIFICATION_ID = "bingSiteVerificationId";
    private static final String DISQUS_UNIQUE_URL = "disqusUniqueUrl";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SiteRepository siteRepositoryMock;
    private SiteService siteService;

    public SiteServiceTest() {
        this.siteRepositoryMock = mock(SiteRepository.class);
        this.siteService = new SiteServiceImpl(siteRepositoryMock, new MappingConfig().createModelMapper());
    }

    @Test
    public void shouldReturnFirstSiteWhenSiteExistInDatabase() {

        // Given

        Site firstSite = buildSite();

        Site secondSite = buildSite();
        secondSite.setName("Second Site!");

        when(siteRepositoryMock.findAll()).thenReturn(Arrays.asList(firstSite, secondSite));

        // When

        SiteQueryDTO siteQueryDTO = siteService.getSite();

        // Then

        assertEquals(siteQueryDTO.getName(), firstSite.getName());
        assertEquals(siteQueryDTO.getDescription(), firstSite.getDescription());
        assertEquals(siteQueryDTO.getSiteUrl(), firstSite.getSiteUrl());
        assertEquals(siteQueryDTO.getHomeUrl(), firstSite.getHomeUrl());
        assertEquals(siteQueryDTO.getMetaKeywords(), firstSite.getAdditionalProperties().get(META_KEYWORDS));
        assertEquals(siteQueryDTO.getSocialLinkedInUrl(), firstSite.getAdditionalProperties().get(SOCIAL_LINKED_IN_URL));
        assertEquals(siteQueryDTO.getSocialGithubUrl(), firstSite.getAdditionalProperties().get(SOCIAL_GITHUB_URL));
        assertEquals(siteQueryDTO.getSocialFBUrl(), firstSite.getAdditionalProperties().get(SOCIAL_FB_URL));
        assertEquals(siteQueryDTO.getSocialTwitterUrl(), firstSite.getAdditionalProperties().get(SOCIAL_TWITTER_URL));
        assertEquals(siteQueryDTO.getSocialGooglePlusUrl(), firstSite.getAdditionalProperties().get(SOCIAL_GOOGLE_PLUS_URL));
        assertEquals(siteQueryDTO.getSocialWikiPageUrl(), firstSite.getAdditionalProperties().get(SOCIAL_WIKI_PAGE_URL));
        assertEquals(siteQueryDTO.getSocialEmailMe(), firstSite.getAdditionalProperties().get(SOCIAL_EMAIL_ME));
        assertEquals(siteQueryDTO.getGoogleAnalyticsAccountId(), firstSite.getAdditionalProperties().get(GOOGLE_ANALYTICS_ACCOUNT_ID));
        assertEquals(siteQueryDTO.getGoogleSiteVerificationId(), firstSite.getAdditionalProperties().get(GOOGLE_SITE_VERIFICATION_ID));
        assertEquals(siteQueryDTO.getBingSiteVerificationId(), firstSite.getAdditionalProperties().get(BING_SITE_VERIFICATION_ID));
        assertEquals(siteQueryDTO.getDisqusUniqueUrl(), firstSite.getAdditionalProperties().get(DISQUS_UNIQUE_URL));
    }

    @Test
    public void shouldThrowExceptionWhenSiteNotExistInDatabase() {

        // Given

        thrown.expect(SiteNotFoundException.class);
        thrown.expectMessage("Site information should be pre persisted using init data mechanism.");

        when(siteRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        // When

        SiteQueryDTO siteQueryDTO = siteService.getSite();

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldAbleToUpdateSiteWhenSiteInDatabase() {

        // Given

        ArgumentCaptor<Site> siteRepoSaveArg = ArgumentCaptor.forClass(Site.class);

        Site site = buildSite();
        when(siteRepositoryMock.findAll()).thenReturn(Collections.singletonList(site));
        when(siteRepositoryMock.save(any())).thenReturn(site);

        // When

        SiteUpdateCommandDTO siteUpdateCommandDTO = buildSiteUpdateCommandDTO();
        siteService.updateSite(siteUpdateCommandDTO);

        // Then

        verify(siteRepositoryMock).save(siteRepoSaveArg.capture());
        Site capturedSite = siteRepoSaveArg.getValue();

        assertEquals(capturedSite.getName(), siteUpdateCommandDTO.getName());
        assertEquals(capturedSite.getDescription(), siteUpdateCommandDTO.getDescription());
        assertEquals(capturedSite.getSiteUrl(), siteUpdateCommandDTO.getSiteUrl());
        assertEquals(capturedSite.getHomeUrl(), siteUpdateCommandDTO.getHomeUrl());

        assertEquals(capturedSite.getAdditionalProperties().get(META_KEYWORDS),
                siteUpdateCommandDTO.getMetaKeywords());

        assertEquals(capturedSite.getAdditionalProperties().get(SOCIAL_LINKED_IN_URL),
                siteUpdateCommandDTO.getSocialLinkedInUrl());

        assertEquals(capturedSite.getAdditionalProperties().get(SOCIAL_GITHUB_URL),
                siteUpdateCommandDTO.getSocialGithubUrl());

        assertEquals(capturedSite.getAdditionalProperties().get(SOCIAL_FB_URL), siteUpdateCommandDTO.getSocialFBUrl());

        assertEquals(capturedSite.getAdditionalProperties().get(SOCIAL_TWITTER_URL),
                siteUpdateCommandDTO.getSocialTwitterUrl());

        assertEquals(capturedSite.getAdditionalProperties().get(SOCIAL_GOOGLE_PLUS_URL),
                siteUpdateCommandDTO.getSocialGooglePlusUrl());

        assertEquals(capturedSite.getAdditionalProperties().get(SOCIAL_WIKI_PAGE_URL),
                siteUpdateCommandDTO.getSocialWikiPageUrl());

        assertEquals(capturedSite.getAdditionalProperties().get(SOCIAL_EMAIL_ME),
                siteUpdateCommandDTO.getSocialEmailMe());

        assertEquals(capturedSite.getAdditionalProperties().get(GOOGLE_ANALYTICS_ACCOUNT_ID),
                siteUpdateCommandDTO.getGoogleAnalyticsAccountId());

        assertEquals(capturedSite.getAdditionalProperties().get(GOOGLE_SITE_VERIFICATION_ID),
                siteUpdateCommandDTO.getGoogleSiteVerificationId());

        assertEquals(capturedSite.getAdditionalProperties().get(BING_SITE_VERIFICATION_ID),
                siteUpdateCommandDTO.getBingSiteVerificationId());

        assertEquals(capturedSite.getAdditionalProperties().get(DISQUS_UNIQUE_URL),
                siteUpdateCommandDTO.getDisqusUniqueUrl());
    }

    @Test
    public void shouldThrowExceptionWhenSiteNotExistInDatabaseWhileUpdate() {

        // Given

        thrown.expect(SiteNotFoundException.class);
        thrown.expectMessage("Site information should be pre persisted using init data mechanism.");

        when(siteRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        // When

        siteService.updateSite(buildSiteUpdateCommandDTO());

        // Then
        // Expect test to be passed.

    }

    private Site buildSite() {

        Site site = new Site();
        site.setId(1L);
        site.setName("Hello World!");
        site.setDescription("Learn and Grow!");
        site.setSiteUrl("localhost:8080");
        site.setHomeUrl("localhost:8080");

        Map<String, String> additionalProps = new HashMap<>(12);
        additionalProps.put(META_KEYWORDS, "keyword");
        additionalProps.put(SOCIAL_LINKED_IN_URL, "http://linkedin.com/openblog-new");
        additionalProps.put(SOCIAL_GITHUB_URL, "http://github.com/mmahmood/openblog-new");
        additionalProps.put(SOCIAL_FB_URL, "http://facebook.com/openblog-new");
        additionalProps.put(SOCIAL_TWITTER_URL, "http://twitter.com/openblog-new");
        additionalProps.put(SOCIAL_GOOGLE_PLUS_URL, "http://gplus.com/openblog-new");
        additionalProps.put(SOCIAL_WIKI_PAGE_URL, "http://wiki.com/openblog-new");
        additionalProps.put(SOCIAL_EMAIL_ME, "hello@openblog.com");
        additionalProps.put(GOOGLE_ANALYTICS_ACCOUNT_ID, "GA-7891011");
        additionalProps.put(GOOGLE_SITE_VERIFICATION_ID, "GSV-7891011");
        additionalProps.put(BING_SITE_VERIFICATION_ID, "BING-7891011");
        additionalProps.put(DISQUS_UNIQUE_URL, "openblognew.disqus.com");

        site.setAdditionalProperties(additionalProps);

        return site;
    }

    private SiteUpdateCommandDTO buildSiteUpdateCommandDTO() {

        SiteUpdateCommandDTO siteUpdateCommandDTO = new SiteUpdateCommandDTO();
        siteUpdateCommandDTO.setName("New Site");
        siteUpdateCommandDTO.setDescription("Learn how to grow!");
        siteUpdateCommandDTO.setSiteUrl("localhost:8081");
        siteUpdateCommandDTO.setHomeUrl("localhost:8081");
        siteUpdateCommandDTO.setMetaKeywords("keyword1,keyword2");
        siteUpdateCommandDTO.setSocialLinkedInUrl("http://linkedin.com/openblog");
        siteUpdateCommandDTO.setSocialGithubUrl("http://github.com/mmahmood/openblog");
        siteUpdateCommandDTO.setSocialFBUrl("http://facebook.com/openblog");
        siteUpdateCommandDTO.setSocialTwitterUrl("http://twitter.com/openblog");
        siteUpdateCommandDTO.setSocialGooglePlusUrl("http://gplus.com/openblog");
        siteUpdateCommandDTO.setSocialWikiPageUrl("http://wiki.com/openblog");
        siteUpdateCommandDTO.setSocialEmailMe("admin@openblog.com");
        siteUpdateCommandDTO.setGoogleAnalyticsAccountId("GA-123456");
        siteUpdateCommandDTO.setGoogleSiteVerificationId("GSV-123456");
        siteUpdateCommandDTO.setBingSiteVerificationId("BING-123456");
        siteUpdateCommandDTO.setDisqusUniqueUrl("openblog.disqus.com");

        return siteUpdateCommandDTO;
    }
}