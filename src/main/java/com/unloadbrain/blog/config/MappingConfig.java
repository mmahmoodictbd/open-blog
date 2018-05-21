package com.unloadbrain.blog.config;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.Post;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostStatusDTO;
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

        addMappingPostToPostDTO(mapper);
        addMappingPostDTOToPost(mapper);

        return mapper;
    }

    private ModelMapper addMappingPostToPostDTO(ModelMapper mapper) {

        // TODO:: Find better solution
        Converter<Set, String> convertCategoriesAndTags = mappingContext -> {

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


        PropertyMap propertyMap = new PropertyMap<Post, PostDTO>() {

            @Override
            protected void configure() {
                skip(destination.getAction());
                skip(destination.getStatus());
            }
        };

        mapper.createTypeMap(Post.class, PostDTO.class)
                .addMapping(source -> source.getId(), PostDTO::setId)
                .addMapping(source -> source.getTitle(), PostDTO::setTitle)
                .addMapping(source -> source.getSummary(), PostDTO::setSummary)
                .addMapping(source -> source.getContent(), PostDTO::setContent)
                .addMapping(source -> source.getPermalink(), PostDTO::setPermalink)
                .addMapping(source -> source.getFeatureImageLink(), PostDTO::setFeatureImageLink)
                .addMapping(source -> source.getCreatedAt(), PostDTO::setCreatedAt)
                .addMapping(source -> source.getUpdatedAt(), PostDTO::setUpdatedAt)
                .addMappings(propertyMap);


        mapper.createTypeMap(Set.class, String.class)
                .setConverter(convertCategoriesAndTags);

        mapper.createTypeMap(DraftPost.class, PostDTO.class)
                .addMappings(new PropertyMap<DraftPost, PostDTO>() {
                    @Override
                    protected void configure() {
                        map().setStatus(PostStatusDTO.DRAFT);
                        skip(destination.getAction());
                    }
                });

        mapper.createTypeMap(PublishedPost.class, PostDTO.class)
                .addMappings(new PropertyMap<PublishedPost, PostDTO>() {
                    @Override
                    protected void configure() {
                        map().setStatus(PostStatusDTO.PUBLISHED);
                        skip(destination.getAction());
                    }
                });

        mapper.validate();

        return mapper;
    }

    private ModelMapper addMappingPostDTOToPost(ModelMapper mapper) {

        PropertyMap propertyMap = new PropertyMap<PostDTO, Post>() {

            @Override
            protected void configure() {
                skip(destination.getCategories());
                skip(destination.getTags());
                skip(destination.getCreatedAt());
                skip(destination.getUpdatedAt());
            }
        };

        mapper.createTypeMap(PostDTO.class, Post.class)
                .addMapping(source -> source.getId(), Post::setId)
                .addMapping(source -> source.getTitle(), Post::setTitle)
                .addMapping(source -> source.getSummary(), Post::setSummary)
                .addMapping(source -> source.getContent(), Post::setContent)
                .addMapping(source -> source.getPermalink(), Post::setPermalink)
                .addMapping(source -> source.getFeatureImageLink(), Post::setFeatureImageLink)
                .addMappings(propertyMap);

        mapper.validate();

        return mapper;
    }

}
