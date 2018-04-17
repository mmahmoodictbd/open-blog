package com.unloadbrain.blog.config;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.Post;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.helper.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MappingConfigurationTest {

    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        modelMapper = new MappingConfiguration().createModelMapper();
    }

    @Test
    public void convertPostToPostDTOTest() {

        // Given
        Post post = ObjectFactory.createDraftPost();

        // When
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        // Test
        assertEquals(post.getId(), postDTO.getId());
        assertEquals(post.getTitle(), postDTO.getTitle());
        assertEquals(post.getContent(), postDTO.getContent());
        assertEquals(post.getPermalink(), postDTO.getPermalink());
        assertEquals(post.getFeatureImageLink(), postDTO.getFeatureImageLink());
        assertEquals(post.getStatus().name(), postDTO.getStatus());

        String categorySetToCommaSeparatedString =
                post.getCategories().stream().map(Category::getName).collect(Collectors.joining(","));
        assertEquals(categorySetToCommaSeparatedString, postDTO.getCategories());

        String tagSetToCommaSeparatedString =
                post.getTags().stream().map(Tag::getName).collect(Collectors.joining(","));
        assertEquals(tagSetToCommaSeparatedString, postDTO.getTags());

    }

    @Test
    public void convertPostDTOToPostTest() {

        // Given
        PostDTO postDTO = ObjectFactory.createDraftPostDTO();

        // When
        Post post = modelMapper.map(postDTO, Post.class);

        // Test
        assertEquals(postDTO.getId(), post.getId());
        assertEquals(postDTO.getTitle(), post.getTitle());
        assertEquals(postDTO.getContent(), post.getContent());
        assertEquals(postDTO.getPermalink(), post.getPermalink());
        assertEquals(postDTO.getFeatureImageLink(), post.getFeatureImageLink());
        assertEquals(postDTO.getStatus(), post.getStatus().name());

        Set<Category> commaSeparatedStringToCategorySet = Arrays.stream(postDTO.getCategories().split(","))
                .map(cat -> new Category(){{setName(cat);}})
                .collect(Collectors.toCollection(LinkedHashSet<Category>::new));
        assertThat(commaSeparatedStringToCategorySet, is(post.getCategories()));

        Set<Tag> commaSeparatedStringToTagSet = Arrays.stream(postDTO.getTags().split(","))
                .map(tag -> new Tag(){{setName(tag);}})
                .collect(Collectors.toCollection(LinkedHashSet<Tag>::new));
        assertThat(commaSeparatedStringToTagSet, is(post.getTags()));


    }


}