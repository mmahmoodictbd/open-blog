package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.domain.repository.TagRepository;
import com.unloadbrain.blog.dto.PostActionDTO;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.dto.PostStatusDTO;
import com.unloadbrain.blog.helper.ObjectFactory;
import com.unloadbrain.blog.util.DateUtility;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class PostServiceTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;
    private CategoryRepository categoryRepositoryMock;
    private TagRepository tagRepositoryMock;
    private DateUtility dateUtility;
    private ModelMapper modelMapper;

    private PostService postService;

    public PostServiceTests() {

        this.publishedPostRepository = mock(PublishedPostRepository.class);
        this.draftPostRepository = mock(DraftPostRepository.class);
        this.categoryRepositoryMock = mock(CategoryRepository.class);
        this.tagRepositoryMock = mock(TagRepository.class);
        this.dateUtility = mock(DateUtility.class);
        this.modelMapper = new MappingConfig().createModelMapper();

        this.postService = new PostService(
                publishedPostRepository, draftPostRepository,
                categoryRepositoryMock, tagRepositoryMock,
                dateUtility, modelMapper);
    }

    @Test
    public void testDraftActionForNewDraftPost() {

        // Given

        when(draftPostRepository.save(any())).thenReturn(ObjectFactory.createDraftPost());

        PostDTO postDTO = ObjectFactory.createDraftPostDTO();
        postDTO.setId(null);
        postDTO.setAction(PostActionDTO.DRAFT);

        // When
        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);

        // Then
        assertNotNull("Should have id of created post.", postIdentityDTO.getId());
        assertNotNull("Should have status of created post.", postIdentityDTO.getStatus());
        assertEquals(PostStatusDTO.DRAFT, postIdentityDTO.getStatus());
    }

    @Test
    public void testPublishActionForNewPublishedPost() {

        // Given

        ArgumentCaptor<PublishedPost> publishedPostRepoSaveArg = ArgumentCaptor.forClass(PublishedPost.class);

        when(publishedPostRepository.save(any())).thenReturn(ObjectFactory.createPublishedPost());

        Date now = new Date();
        when(dateUtility.getCurrentDate()).thenReturn(now);

        PostDTO postDTO = ObjectFactory.createPublishedPostDTO();
        postDTO.setId(null);
        postDTO.setAction(PostActionDTO.PUBLISH);

        // When
        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);

        // Then

        assertNotNull("Should have id of created post.", postIdentityDTO.getId());
        assertNotNull("Should have status of created post.", postIdentityDTO.getStatus());
        assertEquals(PostStatusDTO.PUBLISHED, postIdentityDTO.getStatus());

        verify(publishedPostRepository).save(publishedPostRepoSaveArg.capture());
        assertEquals(now.getTime(), publishedPostRepoSaveArg.getValue().getPublishDate().getTime());
    }

    @Test
    public void testDraftActionForExistingDraftPost() {

        // Given

        ArgumentCaptor<DraftPost> draftPostRepoSaveArg = ArgumentCaptor.forClass(DraftPost.class);

        when(draftPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createDraftPost()));
        when(draftPostRepository.save(any())).thenReturn(ObjectFactory.createDraftPost());

        PostDTO postDTO = ObjectFactory.createDraftPostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("SamplePostUpdated");
        postDTO.setAction(PostActionDTO.DRAFT);

        // When
        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(PostStatusDTO.DRAFT, postIdentityDTO.getStatus());

        verify(draftPostRepository).save(draftPostRepoSaveArg.capture());
        assertEquals("SamplePostUpdated", draftPostRepoSaveArg.getValue().getTitle());

    }

    @Test
    public void testDraftActionForExistingPublishedPost() {

        // Given

        ArgumentCaptor<DraftPost> draftPostRepoSaveArg = ArgumentCaptor.forClass(DraftPost.class);

        when(publishedPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createPublishedPost()));
        when(draftPostRepository.save(any())).thenReturn(ObjectFactory.createDraftPost());

        PostDTO postDTO = ObjectFactory.createPublishedPostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("NewDraftTitle");
        postDTO.setAction(PostActionDTO.DRAFT);

        // When
        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(PostStatusDTO.DRAFT, postIdentityDTO.getStatus());

        verify(draftPostRepository).save(draftPostRepoSaveArg.capture());
        assertEquals("NewDraftTitle", draftPostRepoSaveArg.getValue().getTitle());
        assertNotNull(draftPostRepoSaveArg.getValue().getPublishedPost());
    }


    @Test
    public void testPublishActionForExistingDraftPostWithPreExistingPublishedPost() {

        // Given

        ArgumentCaptor<PublishedPost> publishedPostArgCaptor = ArgumentCaptor.forClass(PublishedPost.class);

        PublishedPost publishedPost = ObjectFactory.createPublishedPost();
        DraftPost draftPost = ObjectFactory.createDraftPost();
        draftPost.setPublishedPost(publishedPost);

        when(draftPostRepository.findById(any())).thenReturn(Optional.of(draftPost));
        when(publishedPostRepository.save(any())).thenReturn(publishedPost);
        doNothing().when(draftPostRepository).deleteById(anyLong());

        PostDTO postDTO = ObjectFactory.createDraftPostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("NewPublishedTitle");
        postDTO.setAction(PostActionDTO.PUBLISH);

        // When
        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(PostStatusDTO.PUBLISHED, postIdentityDTO.getStatus());

        verify(publishedPostRepository).save(publishedPostArgCaptor.capture());
        assertEquals("NewPublishedTitle", publishedPostArgCaptor.getValue().getTitle());

    }

    @Test
    public void testPublishActionForExistingDraftPostWithoutPreExistingPublishedPost() {

        // Given

        ArgumentCaptor<PublishedPost> publishedPostArgCaptor = ArgumentCaptor.forClass(PublishedPost.class);

        PublishedPost publishedPost = ObjectFactory.createPublishedPost();
        DraftPost draftPost = ObjectFactory.createDraftPost();

        when(draftPostRepository.findById(any())).thenReturn(Optional.of(draftPost));
        when(publishedPostRepository.save(any())).thenReturn(publishedPost);
        doNothing().when(draftPostRepository).deleteById(anyLong());

        PostDTO postDTO = ObjectFactory.createDraftPostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("NewPublishedTitle");
        postDTO.setAction(PostActionDTO.PUBLISH);

        // When
        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(PostStatusDTO.PUBLISHED, postIdentityDTO.getStatus());

        verify(publishedPostRepository).save(publishedPostArgCaptor.capture());
        assertEquals("NewPublishedTitle", publishedPostArgCaptor.getValue().getTitle());

    }

    @Test
    public void testPublishActionForExistingPublishedPost() {

        // Given

        ArgumentCaptor<PublishedPost> publishedPostArgCaptor = ArgumentCaptor.forClass(PublishedPost.class);

        when(publishedPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createPublishedPost()));
        when(publishedPostRepository.save(any())).thenReturn(ObjectFactory.createPublishedPost());

        PostDTO postDTO = ObjectFactory.createPublishedPostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("NewPublishedTitle");
        postDTO.setAction(PostActionDTO.PUBLISH);

        // When
        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(PostStatusDTO.PUBLISHED, postIdentityDTO.getStatus());

        verify(publishedPostRepository).save(publishedPostArgCaptor.capture());
        assertEquals("NewPublishedTitle", publishedPostArgCaptor.getValue().getTitle());

    }

    @Test
    public void testGetPostWhenStatusIsDraft() {

        // Given
        when(draftPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createDraftPost()));

        // When
        PostDTO postDTO = postService.getPost(1L, PostStatusDTO.DRAFT);

        // Then
        assertEquals(1, postDTO.getId().longValue());

    }

    @Test
    public void testGetPostWhenStatusIsPublished() {

        // Given
        when(publishedPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createPublishedPost()));

        // When
        PostDTO postDTO = postService.getPost(1L, PostStatusDTO.PUBLISHED);

        // Then
        assertEquals(1, postDTO.getId().longValue());

    }

    @Test
    public void throwExceptionPostCouldNotBeFound() throws IllegalStateException {

        // Given

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Could not find the post.");

        when(publishedPostRepository.findById(any())).thenReturn(Optional.empty());

        // When
        PostDTO postDTO = postService.getPost(1L, PostStatusDTO.PUBLISHED);

        // Then
        // Expect test to be passed.

    }
}
