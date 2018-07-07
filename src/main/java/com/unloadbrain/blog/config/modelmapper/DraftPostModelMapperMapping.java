package com.unloadbrain.blog.config.modelmapper;

import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class DraftPostModelMapperMapping implements ModelMapperMapping {

    @Override
    public void appendToModelMapper(ModelMapper modelMapper) {

        addMappingDraftPostToPostDTO(modelMapper);
        addMappingPostDTOToDraftPost(modelMapper);
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
}
