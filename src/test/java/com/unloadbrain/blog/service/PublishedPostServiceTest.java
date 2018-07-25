package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.exception.DraftPostNotFoundException;
import com.unloadbrain.blog.exception.InvalidPostStatusException;
import com.unloadbrain.blog.exception.PublishedPostNotFoundException;
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


public class PublishedPostServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CategoryService categoryService;
    private TagService tagService;

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;

    private DateUtility dateUtility;
    private ModelMapper modelMapper;

    private PublishPostService publishPostService;

    public PublishedPostServiceTest() {

        this.categoryService = mock(CategoryServiceImpl.class);
        this.tagService = mock(TagServiceImpl.class);
        this.publishedPostRepository = mock(PublishedPostRepository.class);
        this.draftPostRepository = mock(DraftPostRepository.class);
        this.dateUtility = mock(DateUtility.class);
        this.modelMapper = new MappingConfig().createModelMapper();

        this.publishPostService = new PublishPostServiceImpl(categoryService, tagService,
                publishedPostRepository, draftPostRepository, dateUtility, modelMapper);
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
        postDTO.setStatus(CurrentPostStatus.NEW);

        // When
        PostIdentityDTO postIdentityDTO = publishPostService.createUpdatePost(postDTO);

        // Then

        assertNotNull("Should have id of created post.", postIdentityDTO.getId());
        assertNotNull("Should have status of created post.", postIdentityDTO.getStatus());
        assertEquals(CurrentPostStatus.PUBLISHED, postIdentityDTO.getStatus());

        verify(publishedPostRepository).save(publishedPostRepoSaveArg.capture());
        assertEquals(now.getTime(), publishedPostRepoSaveArg.getValue().getPublishDate().getTime());
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

        // When
        PostIdentityDTO postIdentityDTO = publishPostService.createUpdatePost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(CurrentPostStatus.PUBLISHED, postIdentityDTO.getStatus());

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

        // When
        PostIdentityDTO postIdentityDTO = publishPostService.createUpdatePost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(CurrentPostStatus.PUBLISHED, postIdentityDTO.getStatus());

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

        // When
        PostIdentityDTO postIdentityDTO = publishPostService.createUpdatePost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(CurrentPostStatus.PUBLISHED, postIdentityDTO.getStatus());

        verify(publishedPostRepository).save(publishedPostArgCaptor.capture());
        assertEquals("NewPublishedTitle", publishedPostArgCaptor.getValue().getTitle());

    }

    @Test
    public void shouldSetPermalinkFromTitleIfNull() {

        // Given

        ArgumentCaptor<PublishedPost> publishedPostRepoSaveArg = ArgumentCaptor.forClass(PublishedPost.class);

        when(draftPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createDraftPost()));
        when(publishedPostRepository.save(any())).thenReturn(ObjectFactory.createPublishedPost());

        // When

        PostDTO draftPostDTO = ObjectFactory.createDraftPostDTO();
        draftPostDTO.setTitle("New Title");
        draftPostDTO.setPermalink(null);

        publishPostService.createUpdatePost(draftPostDTO);

        // Then

        verify(publishedPostRepository).save(publishedPostRepoSaveArg.capture());
        assertEquals("new-title", publishedPostRepoSaveArg.getValue().getPermalink());

    }

    @Test
    public void shouldThrowExceptionWhenDraftPostNotFoundById() {

        // Given

        thrown.expect(DraftPostNotFoundException.class);
        thrown.expectMessage("Could not found draft post id - 1");

        when(draftPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When

        publishPostService.createUpdatePost(ObjectFactory.createDraftPostDTO());

        // Then
        // Expect test to be passed.

    }

    @Test
    public void shouldThrowExceptionWhenPublishedPostNotFoundById() {

        // Given

        thrown.expect(PublishedPostNotFoundException.class);
        thrown.expectMessage("Could not found published post id - 1");

        when(publishedPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When

        publishPostService.createUpdatePost(ObjectFactory.createPublishedPostDTO());

        // Then
        // Expect test to be passed.

    }

    @Test
    public void shouldThrowExceptionWhenPostHasUnknownStatus() {

        // Given

        thrown.expect(InvalidPostStatusException.class);
        thrown.expectMessage("Post current status should be either NEW, PUBLISHED or DRAFT.");

        // When

        publishPostService.createUpdatePost(ObjectFactory.createPostDTOWithoutStatus());

        // Then
        // Expect test to be passed.

    }

    @Test
    public void shouldReturnPublishedPostDtoById() {

        // Given
        PublishedPost publishedPost = ObjectFactory.createPublishedPost();
        when(publishedPostRepository.findById(any())).thenReturn(Optional.of(publishedPost));

        // When

        PostDTO postDTO = publishPostService.getPost(1L);

        // Then

        assertEquals(postDTO.getId(), publishedPost.getId());
        // Improve here after kicking out PostDTO

    }

    @Test
    public void shouldThrowExceptionWhenPublishedPostDtoById() {

        // Given

        thrown.expect(PublishedPostNotFoundException.class);
        thrown.expectMessage("Could not found published post id - 1");

        when(publishedPostRepository.findById(any())).thenReturn(Optional.empty());

        // When

        publishPostService.getPost(1L);

        // Then
        // Expect test to be passed.

    }
}
