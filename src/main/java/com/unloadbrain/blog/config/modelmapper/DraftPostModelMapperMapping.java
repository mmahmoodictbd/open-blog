package com.unloadbrain.blog.config.modelmapper;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
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

public class DraftPostModelMapperMapping implements ModelMapperMapping {

    private static final String SEO_TITLE = "seoTitle";
    private static final String SEO_SLUG = "seoSlug";
    private static final String SEO_DESCRIPTION = "seoDescription";
    private static final String SEO_TAGS = "seoTags";

    @Override
    public void appendToModelMapper(ModelMapper modelMapper) {

        addMappingDraftPostToPostDTO(modelMapper);
        addMappingPostDTOToDraftPost(modelMapper);

        addMappingDraftPostToPostQueryDTO(modelMapper);
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

    private ModelMapper addMappingDraftPostToPostQueryDTO(ModelMapper mapper) {

        mapper.createTypeMap(DraftPost.class, PostQueryDTO.class).setConverter(getConverterDraftPostToPostQueryDTO());
        mapper.validate();
        return mapper;
    }

    private Converter<DraftPost, PostQueryDTO> getConverterDraftPostToPostQueryDTO() {

        return mappingContext -> {

            DraftPost draftPost = mappingContext.getSource();
            PostQueryDTO postQueryDTO = mappingContext.getDestination();

            postQueryDTO.setId(draftPost.getId());
            postQueryDTO.setTitle(draftPost.getTitle());
            postQueryDTO.setContent(draftPost.getContent());
            postQueryDTO.setSummary(draftPost.getSummary());
            postQueryDTO.setCategories(convertCategorySetToCommaSeparatedString(draftPost.getCategories()));
            postQueryDTO.setTags(convertTagSetToCommaSeparatedString(draftPost.getTags()));
            postQueryDTO.setPermalink(draftPost.getPermalink());
            postQueryDTO.setFeatureImageLink(draftPost.getFeatureImageLink());
            postQueryDTO.setStatus(CurrentPostStatus.DRAFT);
            postQueryDTO.setSeoTitle(draftPost.getAdditionalProperties().get(SEO_TITLE));
            postQueryDTO.setSeoSlug(draftPost.getAdditionalProperties().get(SEO_SLUG));
            postQueryDTO.setSeoDescription(draftPost.getAdditionalProperties().get(SEO_DESCRIPTION));
            postQueryDTO.setSeoTags(draftPost.getAdditionalProperties().get(SEO_TAGS));
            postQueryDTO.setCreatedAt(draftPost.getCreatedAt());
            postQueryDTO.setUpdatedAt(draftPost.getUpdatedAt());

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
