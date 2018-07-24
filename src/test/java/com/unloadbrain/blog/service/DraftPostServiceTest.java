package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.exception.DraftPostNotFoundException;
import com.unloadbrain.blog.exception.InvalidPostStatusException;
import com.unloadbrain.blog.exception.PublishedPostNotFoundException;
import com.unloadbrain.blog.helper.ObjectFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DraftPostServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CategoryService categoryServiceMock;
    private TagService tagServiceMock;

    private PublishedPostRepository publishedPostRepositoryMock;
    private DraftPostRepository draftPostRepositoryMock;

    private ModelMapper modelMapper;

    private DraftPostService draftPostService;

    public DraftPostServiceTest() {

        this.categoryServiceMock = mock(CategoryServiceImpl.class);
        this.tagServiceMock = mock(TagServiceImpl.class);
        this.publishedPostRepositoryMock = mock(PublishedPostRepository.class);
        this.draftPostRepositoryMock = mock(DraftPostRepository.class);
        this.modelMapper = new MappingConfig().createModelMapper();

        this.draftPostService = new DraftPostServiceImpl(
                categoryServiceMock, tagServiceMock,
                publishedPostRepositoryMock, draftPostRepositoryMock, modelMapper);
    }

    @Test
    public void testDraftActionForNewDraftPost() {

        // Given

        when(draftPostRepositoryMock.save(any())).thenReturn(ObjectFactory.createDraftPost());

        PostDTO postDTO = ObjectFactory.createDraftPostDTO();
        postDTO.setId(null);
        postDTO.setStatus(CurrentPostStatus.NEW);

        // When
        PostIdentityDTO postIdentityDTO = draftPostService.draftPost(postDTO);

        // Then
        assertNotNull("Should have id of created post.", postIdentityDTO.getId());
        assertNotNull("Should have status of created post.", postIdentityDTO.getStatus());
        assertEquals(CurrentPostStatus.DRAFT, postIdentityDTO.getStatus());
    }

    @Test
    public void testDraftActionForExistingDraftPost() {

        // Given

        ArgumentCaptor<DraftPost> draftPostRepoSaveArg = ArgumentCaptor.forClass(DraftPost.class);

        when(draftPostRepositoryMock.findById(any())).thenReturn(Optional.of(ObjectFactory.createDraftPost()));
        when(draftPostRepositoryMock.save(any())).thenReturn(ObjectFactory.createDraftPost());

        PostDTO postDTO = ObjectFactory.createDraftPostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("SamplePostUpdated");

        // When
        PostIdentityDTO postIdentityDTO = draftPostService.draftPost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(CurrentPostStatus.DRAFT, postIdentityDTO.getStatus());

        verify(draftPostRepositoryMock).save(draftPostRepoSaveArg.capture());
        assertEquals("SamplePostUpdated", draftPostRepoSaveArg.getValue().getTitle());

    }

    @Test
    public void testDraftActionForExistingPublishedPost() {

        // Given

        ArgumentCaptor<DraftPost> draftPostRepoSaveArg = ArgumentCaptor.forClass(DraftPost.class);

        when(publishedPostRepositoryMock.findById(any())).thenReturn(Optional.of(ObjectFactory.createPublishedPost()));
        when(draftPostRepositoryMock.save(any())).thenReturn(ObjectFactory.createDraftPost());

        PostDTO postDTO = ObjectFactory.createPublishedPostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("NewDraftTitle");

        // When
        PostIdentityDTO postIdentityDTO = draftPostService.draftPost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(CurrentPostStatus.DRAFT, postIdentityDTO.getStatus());

        verify(draftPostRepositoryMock).save(draftPostRepoSaveArg.capture());
        assertEquals("NewDraftTitle", draftPostRepoSaveArg.getValue().getTitle());
        assertNotNull(draftPostRepoSaveArg.getValue().getPublishedPost());
    }

    @Test
    public void shouldSetPermalinkFromTitleIfNull() {

        // Given

        ArgumentCaptor<DraftPost> draftPostRepoSaveArg = ArgumentCaptor.forClass(DraftPost.class);

        when(draftPostRepositoryMock.findById(any())).thenReturn(Optional.of(ObjectFactory.createDraftPost()));
        when(draftPostRepositoryMock.save(any())).thenReturn(ObjectFactory.createDraftPost());

        // When

        PostDTO draftPostDTO = ObjectFactory.createDraftPostDTO();
        draftPostDTO.setTitle("New Title");
        draftPostDTO.setPermalink(null);

        draftPostService.draftPost(draftPostDTO);

        // Then

        verify(draftPostRepositoryMock).save(draftPostRepoSaveArg.capture());
        assertEquals("new-title", draftPostRepoSaveArg.getValue().getPermalink());

    }

    @Test
    public void shouldThrowExceptionWhenDraftPostNotFoundById() {

        // Given

        thrown.expect(DraftPostNotFoundException.class);
        thrown.expectMessage("Could not found draft post id - 1");

        when(draftPostRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        // When

        draftPostService.draftPost(ObjectFactory.createDraftPostDTO());

        // Then
        // Expect test to be passed.

    }

    @Test
    public void shouldThrowExceptionWhenPublishedPostNotFoundById() {

        // Given

        thrown.expect(PublishedPostNotFoundException.class);
        thrown.expectMessage("Could not found published post id - 1");

        when(publishedPostRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        // When

        draftPostService.draftPost(ObjectFactory.createPublishedPostDTO());

        // Then
        // Expect test to be passed.

    }

    @Test
    public void shouldThrowExceptionWhenPostHasUnknownStatus() {

        // Given

        thrown.expect(InvalidPostStatusException.class);
        thrown.expectMessage("Post current status should be either NEW, PUBLISHED or DRAFT.");

        // When

        draftPostService.draftPost(ObjectFactory.createPostDTOWithoutStatus());

        // Then
        // Expect test to be passed.

    }
}
