package com.unloadbrain.blog.config;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.dto.CreateUpdatePostRequest;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class MappingConfig {

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

}
