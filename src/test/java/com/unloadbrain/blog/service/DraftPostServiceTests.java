package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DraftPostServiceTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CategoryService categoryService;
    private TagService tagService;

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;

    private ModelMapper modelMapper;

    private DraftPostService draftPostService;

    public DraftPostServiceTests() {

        this.categoryService = mock(CategoryService.class);
        this.tagService = mock(TagService.class);
        this.publishedPostRepository = mock(PublishedPostRepository.class);
        this.draftPostRepository = mock(DraftPostRepository.class);
        this.modelMapper = new MappingConfig().createModelMapper();

        this.draftPostService = new DraftPostService(
                categoryService, tagService,
                publishedPostRepository, draftPostRepository, modelMapper);
    }

    @Test
    public void testDraftActionForNewDraftPost() {

        // Given

        when(draftPostRepository.save(any())).thenReturn(ObjectFactory.createDraftPost());

        PostDTO postDTO = ObjectFactory.createDraftPostDTO();
        postDTO.setId(null);

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

        when(draftPostRepository.findById(any())).thenReturn(Optional.of(ObjectFactory.createDraftPost()));
        when(draftPostRepository.save(any())).thenReturn(ObjectFactory.createDraftPost());

        PostDTO postDTO = ObjectFactory.createDraftPostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("SamplePostUpdated");

        // When
        PostIdentityDTO postIdentityDTO = draftPostService.draftPost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(CurrentPostStatus.DRAFT, postIdentityDTO.getStatus());

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

        // When
        PostIdentityDTO postIdentityDTO = draftPostService.draftPost(postDTO);

        // Then

        assertEquals(1, postIdentityDTO.getId().longValue());
        assertEquals(CurrentPostStatus.DRAFT, postIdentityDTO.getStatus());

        verify(draftPostRepository).save(draftPostRepoSaveArg.capture());
        assertEquals("NewDraftTitle", draftPostRepoSaveArg.getValue().getTitle());
        assertNotNull(draftPostRepoSaveArg.getValue().getPublishedPost());
    }


}
