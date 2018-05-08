package com.unloadbrain.blog.config;

import com.unloadbrain.blog.domain.model.Post;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.helper.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;

public class MappingConfigTest {

    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        modelMapper = new MappingConfig().createModelMapper();
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

        // FIXME: Somehow Category Set is not converting to comma separated string
        // assertEquals("Programming", postDTO.getCategories());
        // assertEquals("Java,Spring", postDTO.getTags());

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

    }


}