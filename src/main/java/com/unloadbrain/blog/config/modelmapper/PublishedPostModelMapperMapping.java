package com.unloadbrain.blog.config.modelmapper;

import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class PublishedPostModelMapperMapping implements ModelMapperMapping {

    @Override
    public void appendToModelMapper(ModelMapper modelMapper) {

        addMappingPublishedPostToPostDTO(modelMapper);
        addMappingPostDTOToPublishedPost(modelMapper);
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
}
