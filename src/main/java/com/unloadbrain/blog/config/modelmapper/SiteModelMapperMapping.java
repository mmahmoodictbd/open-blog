package com.unloadbrain.blog.config.modelmapper;

import com.unloadbrain.blog.domain.model.Site;
import com.unloadbrain.blog.dto.SiteQueryDTO;
import com.unloadbrain.blog.dto.SiteUpdateCommandDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.HashMap;
import java.util.Map;

public class SiteModelMapperMapping implements ModelMapperMapping {

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

    @Override
    public void appendToModelMapper(ModelMapper modelMapper) {
        addMappingSiteUpdateCommandDTOToSite(modelMapper);
        addMappingSiteToSiteQueryDTO(modelMapper);
    }

    private ModelMapper addMappingSiteToSiteQueryDTO(ModelMapper mapper) {

        mapper.createTypeMap(Site.class, SiteQueryDTO.class)
                .setConverter(getConverterSiteToSiteQueryDTO());

        mapper.validate();

        return mapper;
    }

    private Converter<Site, SiteQueryDTO> getConverterSiteToSiteQueryDTO() {

        return mappingContext -> {

            Site site = mappingContext.getSource();
            SiteQueryDTO siteQueryDTO = mappingContext.getDestination();

            siteQueryDTO.setName(site.getName());
            siteQueryDTO.setDescription(site.getDescription());
            siteQueryDTO.setHomeUrl(site.getHomeUrl());
            siteQueryDTO.setSiteUrl(site.getSiteUrl());

            Map<String, String> additionalProperties = site.getAdditionalProperties();
            siteQueryDTO.setMetaKeywords(additionalProperties.get(META_KEYWORDS));
            siteQueryDTO.setSocialLinkedInUrl(additionalProperties.get(SOCIAL_LINKED_IN_URL));
            siteQueryDTO.setSocialGithubUrl(additionalProperties.get(SOCIAL_GITHUB_URL));
            siteQueryDTO.setSocialFBUrl(additionalProperties.get(SOCIAL_FB_URL));
            siteQueryDTO.setSocialTwitterUrl(additionalProperties.get(SOCIAL_TWITTER_URL));
            siteQueryDTO.setSocialGooglePlusUrl(additionalProperties.get(SOCIAL_GOOGLE_PLUS_URL));
            siteQueryDTO.setSocialWikiPageUrl(additionalProperties.get(SOCIAL_WIKI_PAGE_URL));
            siteQueryDTO.setSocialEmailMe(additionalProperties.get(SOCIAL_EMAIL_ME));
            siteQueryDTO.setGoogleAnalyticsAccountId(additionalProperties.get(GOOGLE_ANALYTICS_ACCOUNT_ID));
            siteQueryDTO.setGoogleSiteVerificationId(additionalProperties.get(GOOGLE_SITE_VERIFICATION_ID));
            siteQueryDTO.setBingSiteVerificationId(additionalProperties.get(BING_SITE_VERIFICATION_ID));

            return siteQueryDTO;
        };
    }


    private ModelMapper addMappingSiteUpdateCommandDTOToSite(ModelMapper mapper) {

        PropertyMap propertyMap = new PropertyMap<SiteUpdateCommandDTO, Site>() {

            @Override
            protected void configure() {

                skip(destination.getId());
                skip(destination.getCreatedAt());
                skip(destination.getUpdatedAt());
            }
        };

        mapper.createTypeMap(SiteUpdateCommandDTO.class, Site.class)
                .setConverter(getConverterSiteUpdateCommandDTOToSite())
                .addMappings(propertyMap);

        mapper.validate();

        return mapper;
    }


    private Converter<SiteUpdateCommandDTO, Site> getConverterSiteUpdateCommandDTOToSite() {

        return mappingContext -> {

            SiteUpdateCommandDTO siteUpdateCommandDTO = mappingContext.getSource();
            Site site = mappingContext.getDestination();

            Map<String, String> additionalProperties = new HashMap<>(11);

            additionalProperties.put(META_KEYWORDS, siteUpdateCommandDTO.getMetaKeywords());
            additionalProperties.put(SOCIAL_LINKED_IN_URL, siteUpdateCommandDTO.getSocialLinkedInUrl());
            additionalProperties.put(SOCIAL_GITHUB_URL, siteUpdateCommandDTO.getSocialGithubUrl());
            additionalProperties.put(SOCIAL_FB_URL, siteUpdateCommandDTO.getSocialFBUrl());
            additionalProperties.put(SOCIAL_TWITTER_URL, siteUpdateCommandDTO.getSocialTwitterUrl());
            additionalProperties.put(SOCIAL_GOOGLE_PLUS_URL, siteUpdateCommandDTO.getSocialGooglePlusUrl());
            additionalProperties.put(SOCIAL_WIKI_PAGE_URL, siteUpdateCommandDTO.getSocialWikiPageUrl());
            additionalProperties.put(SOCIAL_EMAIL_ME, siteUpdateCommandDTO.getSocialEmailMe());
            additionalProperties.put(GOOGLE_ANALYTICS_ACCOUNT_ID, siteUpdateCommandDTO.getGoogleAnalyticsAccountId());
            additionalProperties.put(GOOGLE_SITE_VERIFICATION_ID, siteUpdateCommandDTO.getGoogleSiteVerificationId());
            additionalProperties.put(BING_SITE_VERIFICATION_ID, siteUpdateCommandDTO.getBingSiteVerificationId());

            site.setAdditionalProperties(additionalProperties);

            return site;
        };
    }
}
