package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostQueryDTO;
import com.unloadbrain.blog.exception.DraftPostNotFoundException;
import com.unloadbrain.blog.exception.PublishedPostNotFoundException;
import com.unloadbrain.blog.helper.ObjectFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PostServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;
    private PostRepository postRepository;

    private ModelMapper modelMapper;

    private PostService postService;

    public PostServiceTest() {

        this.publishedPostRepository = mock(PublishedPostRepository.class);
        this.draftPostRepository = mock(DraftPostRepository.class);
        this.postRepository = mock(PostRepository.class);
        this.modelMapper = new MappingConfig().createModelMapper();

        this.postService = new PostServiceImpl(
                publishedPostRepository, draftPostRepository,
                postRepository, modelMapper);

    }

    @Test
    public void testGetPostWhenStatusIsDraft() {

        // Given
        when(draftPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createDraftPost()));

        // When
        PostQueryDTO postQueryDTO = postService.getPost(1L, CurrentPostStatus.DRAFT);

        // Then
        assertEquals(1, postQueryDTO.getId().longValue());

    }

    @Test
    public void testGetPostWhenStatusIsPublished() {

        // Given
        when(publishedPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createPublishedPost()));

        // When
        PostQueryDTO postQueryDTO = postService.getPost(1L, CurrentPostStatus.PUBLISHED);

        // Then
        assertEquals(1, postQueryDTO.getId().longValue());

    }

    @Test
    public void shouldThrowExceptionWhenPublishedPostNotFoundById() throws IllegalStateException {

        // Given

        thrown.expect(PublishedPostNotFoundException.class);
        thrown.expectMessage("Could not find the published post.");

        when(publishedPostRepository.findById(any())).thenReturn(Optional.empty());

        // When
        postService.getPost(1L, CurrentPostStatus.PUBLISHED);

        // Then
        // Expect test to be passed.

    }

    @Test
    public void shouldThrowExceptionWhenDraftPostNotFoundById() throws IllegalStateException {

        // Given

        thrown.expect(DraftPostNotFoundException.class);
        thrown.expectMessage("Could not find the draft post.");

        when(draftPostRepository.findById(any())).thenReturn(Optional.empty());

        // When
        postService.getPost(1L, CurrentPostStatus.DRAFT);

        // Then
        // Expect test to be passed.

    }
}
