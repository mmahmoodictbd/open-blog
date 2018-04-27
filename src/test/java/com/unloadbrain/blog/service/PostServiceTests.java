package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.model.Post;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.domain.repository.PostRepository;
import com.unloadbrain.blog.domain.repository.TagRepository;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.helper.ObjectFactory;
import com.unloadbrain.blog.util.DateUtility;
import org.junit.Ignore;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PostServiceTests {

    private PostRepository postRepositoryMock;
    private CategoryRepository categoryRepositoryMock;
    private TagRepository tagRepositoryMock;
    private DateUtility dateUtility;
    private ModelMapper modelMapper;

    private PostService postService;

    public PostServiceTests() {
        this.postRepositoryMock = mock(PostRepository.class);
        this.categoryRepositoryMock = mock(CategoryRepository.class);
        this.tagRepositoryMock = mock(TagRepository.class);
        this.dateUtility = new DateUtility();
        this.modelMapper = new MappingConfig().createModelMapper();
        this.postService = new PostService(postRepositoryMock, categoryRepositoryMock, tagRepositoryMock, dateUtility, modelMapper);
    }

    @Ignore
    @Test
    public void testCreatePost() {

        // Given
        when(postRepositoryMock.save(any())).thenReturn(ObjectFactory.createDraftPost());
        PostDTO postDTO = ObjectFactory.createDraftPostDTO();

        // When
        Post post = postService.createPost(postDTO);

        // Then
        assertNotNull("Saved post should have createAt date.", post.getCreatedAt());
    }


}
