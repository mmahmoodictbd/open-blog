package com.unloadbrain.blog.config;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.model.Site;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.dto.CreateUpdatePostRequest;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.SiteQueryDTO;
import com.unloadbrain.blog.dto.SiteUpdateCommandDTO;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class MappingConfig {

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

    @Bean
    public ModelMapper createModelMapper() {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        addMappingCreateUpdatePostRequestToPostDTO(mapper);

        addMappingCategoriesTagsToCommaSeparatedString(mapper);

        addMappingPublishedPostToPostDTO(mapper);
        addMappingDraftPostToPostDTO(mapper);

        addMappingPostDTOToPublishedPost(mapper);
        addMappingPostDTOToDraftPost(mapper);

        addMappingSiteUpdateCommandDTOToSite(mapper);
        addMappingSiteToSiteQueryDTO(mapper);

        return mapper;
    }

    private ModelMapper addMappingCategoriesTagsToCommaSeparatedString(ModelMapper mapper) {

        Converter<Set, String> convertCategoriesAndTags = getCategoriesTagsConverter();

        mapper.createTypeMap(Set.class, String.class)
                .setConverter(convertCategoriesAndTags);

        mapper.validate();

        return mapper;
    }

    private ModelMapper addMappingPublishedPostToPostDTO(ModelMapper mapper) {

        PropertyMap propertyMap = new PropertyMap<PublishedPost, PostDTO>() {

            @Override
            protected void configure() {
                map().setStatus(CurrentPostStatus.PUBLISHED);
            }
        };

        mapper.createTypeMap(PublishedPost.class, PostDTO.class)
                .addMapping(source -> source.getId(), PostDTO::setId)
                .addMapping(source -> source.getTitle(), PostDTO::setTitle)
                .addMapping(source -> source.getSummary(), PostDTO::setSummary)
                .addMapping(source -> source.getContent(), PostDTO::setContent)
                .addMapping(source -> source.getPermalink(), PostDTO::setPermalink)
                .addMapping(source -> source.getFeatureImageLink(), PostDTO::setFeatureImageLink)
                .addMapping(source -> source.getCreatedAt(), PostDTO::setCreatedAt)
                .addMapping(source -> source.getUpdatedAt(), PostDTO::setUpdatedAt)
                .addMappings(propertyMap);

        mapper.validate();

        return mapper;
    }


    private ModelMapper addMappingDraftPostToPostDTO(ModelMapper mapper) {

        PropertyMap propertyMap = new PropertyMap<DraftPost, PostDTO>() {

            @Override
            protected void configure() {
                map().setStatus(CurrentPostStatus.DRAFT);
            }
        };

        mapper.createTypeMap(DraftPost.class, PostDTO.class)
                .addMapping(source -> source.getId(), PostDTO::setId)
                .addMapping(source -> source.getTitle(), PostDTO::setTitle)
                .addMapping(source -> source.getSummary(), PostDTO::setSummary)
                .addMapping(source -> source.getContent(), PostDTO::setContent)
                .addMapping(source -> source.getPermalink(), PostDTO::setPermalink)
                .addMapping(source -> source.getFeatureImageLink(), PostDTO::setFeatureImageLink)
                .addMapping(source -> source.getCreatedAt(), PostDTO::setCreatedAt)
                .addMapping(source -> source.getUpdatedAt(), PostDTO::setUpdatedAt)
                .addMappings(propertyMap);

        mapper.validate();

        return mapper;
    }

    private Converter<Set, String> getCategoriesTagsConverter() {

        // TODO:: Find better solution

        return mappingContext -> {

            Set<Category> categories = null;
            Set<Tag> tags = null;

            Set categoriesOrTags = mappingContext.getSource();
            for (Object obj : categoriesOrTags) {
                if (obj instanceof Category) {
                    categories = (Set<Category>) categoriesOrTags;
                    break;
                } else if (obj instanceof Tag) {
                    tags = (Set<Tag>) categoriesOrTags;
                    break;
                }
            }

            if (categories != null) {
                List<String> catStringList = categories.stream().map(Category::getName).collect(Collectors.toList());
                return StringUtils.join(catStringList, ",");
            }

            if (tags != null) {
                List<String> tagStringList = tags.stream().map(Tag::getName).collect(Collectors.toList());
                return StringUtils.join(tagStringList, ",");
            }

            return null;
        };
    }

    private ModelMapper addMappingPostDTOToPublishedPost(ModelMapper mapper) {

        PropertyMap propertyMap = new PropertyMap<PostDTO, PublishedPost>() {

            @Override
            protected void configure() {

                // Handled in service layer
                skip(destination.getCategories());
                skip(destination.getTags());
                skip(destination.getPublishDate());

                // Handled by Data JPA audit
                skip(destination.getCreatedAt());
                skip(destination.getUpdatedAt());

            }
        };

        mapper.createTypeMap(PostDTO.class, PublishedPost.class)
                .addMapping(source -> source.getId(), PublishedPost::setId)
                .addMapping(source -> source.getTitle(), PublishedPost::setTitle)
                .addMapping(source -> source.getSummary(), PublishedPost::setSummary)
                .addMapping(source -> source.getContent(), PublishedPost::setContent)
                .addMapping(source -> source.getPermalink(), PublishedPost::setPermalink)
                .addMapping(source -> source.getFeatureImageLink(), PublishedPost::setFeatureImageLink)
                .addMapping(source -> source.getAdditionalProperties(), PublishedPost::setAdditionalProperties)
                .addMappings(propertyMap);

        mapper.validate();

        return mapper;
    }

    private ModelMapper addMappingPostDTOToDraftPost(ModelMapper mapper) {

        PropertyMap propertyMap = new PropertyMap<PostDTO, DraftPost>() {

            @Override
            protected void configure() {

                skip(destination.getPublishedPost());

                // Handled in service layer
                skip(destination.getCategories());
                skip(destination.getTags());

                // Handled by Data JPA audit
                skip(destination.getCreatedAt());
                skip(destination.getUpdatedAt());

            }
        };

        mapper.createTypeMap(PostDTO.class, DraftPost.class)
                .addMapping(source -> source.getId(), DraftPost::setId)
                .addMapping(source -> source.getTitle(), DraftPost::setTitle)
                .addMapping(source -> source.getSummary(), DraftPost::setSummary)
                .addMapping(source -> source.getContent(), DraftPost::setContent)
                .addMapping(source -> source.getPermalink(), DraftPost::setPermalink)
                .addMapping(source -> source.getFeatureImageLink(), DraftPost::setFeatureImageLink)
                .addMapping(source -> source.getAdditionalProperties(), DraftPost::setAdditionalProperties)
                .addMappings(propertyMap);

        mapper.validate();

        return mapper;
    }

    private ModelMapper addMappingCreateUpdatePostRequestToPostDTO(ModelMapper mapper) {

        PropertyMap propertyMap = new PropertyMap<CreateUpdatePostRequest, PostDTO>() {

            @Override
            protected void configure() {
                skip(destination.getCreatedAt());
                skip(destination.getUpdatedAt());
            }
        };

        mapper.createTypeMap(CreateUpdatePostRequest.class, PostDTO.class)
                .addMapping(source -> source.getId(), PostDTO::setId)
                .addMapping(source -> source.getTitle(), PostDTO::setTitle)
                .addMapping(source -> source.getContent(), PostDTO::setContent)
                .addMapping(source -> source.getSummary(), PostDTO::setSummary)
                .addMapping(source -> source.getCategories(), PostDTO::setCategories)
                .addMapping(source -> source.getTags(), PostDTO::setTags)
                .addMapping(source -> source.getPermalink(), PostDTO::setPermalink)
                .addMapping(source -> source.getFeatureImageLink(), PostDTO::setFeatureImageLink)
                .addMappings(propertyMap);

        mapper.validate();

        return mapper;
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
