package com.unloadbrain.blog.config.modelmapper;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostQueryDTO;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.Set;
import java.util.stream.Collectors;

public class PublishedPostModelMapperMapping implements ModelMapperMapping {

    private static final String SEO_TITLE = "seoTitle";
    private static final String SEO_SLUG = "seoSlug";
    private static final String SEO_DESCRIPTION = "seoDescription";
    private static final String SEO_TAGS = "seoTags";

    @Override
    public void appendToModelMapper(ModelMapper modelMapper) {

        addMappingPublishedPostToPostDTO(modelMapper);
        addMappingPostDTOToPublishedPost(modelMapper);

        addMappingPublishedPostToPostQueryDTO(modelMapper);
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

    private ModelMapper addMappingPublishedPostToPostQueryDTO(ModelMapper mapper) {

        mapper.createTypeMap(PublishedPost.class, PostQueryDTO.class).setConverter(getConverterPublishedPostToPostQueryDTO());
        mapper.validate();
        return mapper;
    }

    private Converter<PublishedPost, PostQueryDTO> getConverterPublishedPostToPostQueryDTO() {

        return mappingContext -> {

            PublishedPost publishedPost = mappingContext.getSource();
            PostQueryDTO postQueryDTO = mappingContext.getDestination();

            postQueryDTO.setId(publishedPost.getId());
            postQueryDTO.setTitle(publishedPost.getTitle());
            postQueryDTO.setContent(publishedPost.getContent());
            postQueryDTO.setSummary(publishedPost.getSummary());
            postQueryDTO.setCategories(convertCategorySetToCommaSeparatedString(publishedPost.getCategories()));
            postQueryDTO.setTags(convertTagSetToCommaSeparatedString(publishedPost.getTags()));
            postQueryDTO.setPermalink(publishedPost.getPermalink());
            postQueryDTO.setFeatureImageLink(publishedPost.getFeatureImageLink());
            postQueryDTO.setStatus(CurrentPostStatus.PUBLISHED);
            postQueryDTO.setSeoTitle(publishedPost.getAdditionalProperties().get(SEO_TITLE));
            postQueryDTO.setSeoSlug(publishedPost.getAdditionalProperties().get(SEO_SLUG));
            postQueryDTO.setSeoDescription(publishedPost.getAdditionalProperties().get(SEO_DESCRIPTION));
            postQueryDTO.setSeoTags(publishedPost.getAdditionalProperties().get(SEO_TAGS));
            postQueryDTO.setCreatedAt(publishedPost.getCreatedAt());
            postQueryDTO.setUpdatedAt(publishedPost.getUpdatedAt());

            return postQueryDTO;
        };
    }

    private String convertCategorySetToCommaSeparatedString(Set<Category> categories) {
        return StringUtils.join(categories.stream().map(Category::getName).collect(Collectors.toList()), ",");
    }

    private String convertTagSetToCommaSeparatedString(Set<Tag> tags) {
        return StringUtils.join(tags.stream().map(Tag::getName).collect(Collectors.toList()), ",");
    }
}
