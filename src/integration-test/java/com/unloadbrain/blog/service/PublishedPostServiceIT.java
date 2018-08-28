package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("it")
public class PublishedPostServiceIT {

    private static final String CACHE_POST_BY_ID = "post";
    private static final String CACHE_POSTS_PAGE = "posts";
    private static final String CACHE_POST_PERMALINK_BY_ID = "postPermalink";

    @Autowired
    private PublishedPostRepository publishedPostRepository;

    @Autowired
    private PublishPostService publishPostService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testPostByIdCachedOnFirstCall() {

        // Given

        cacheManager.getCache(CACHE_POST_BY_ID).clear();
        when(publishedPostRepository.findById(anyLong())).thenReturn(Optional.of(buildPublishedPost()));

        // When

        publishPostService.getPost(1L);
        publishPostService.getPost(1L);
        publishPostService.getPost(2L);
        publishPostService.getPost(2L);

        // Then

        assertNotNull(cacheManager.getCache(CACHE_POST_BY_ID));
        verify(publishedPostRepository, times(1)).findById(1L);
        verify(publishedPostRepository, times(1)).findById(2L);

        reset(publishedPostRepository);

    }

    @Test
    public void testEvictCachePostByIdOnCreateUpdatePost() {

        // Given

        cacheManager.getCache(CACHE_POST_BY_ID).clear();
        when(publishedPostRepository.findById(anyLong())).thenReturn(Optional.of(buildPublishedPost()));
        when(publishedPostRepository.save(any())).thenReturn(buildPublishedPost());

        // When

        // publishedPostRepository.findById(2L) called once here
        publishPostService.getPost(2L);

        // publishedPostRepository.findById(1L) called once here
        publishPostService.getPost(1L);

        // publishedPostRepository.findById(1L) called once here
        publishPostService.createUpdatePost(buildPublishedPostDTO());

        // publishedPostRepository.findById(1L) called once here again because cache got evicted
        publishPostService.getPost(1L);

        // should not call publishedPostRepository.findById(2L) bacause it's already cached
        publishPostService.getPost(2L);

        // Then

        assertNotNull(cacheManager.getCache(CACHE_POST_BY_ID));
        verify(publishedPostRepository, times(3)).findById(1L);
        verify(publishedPostRepository, times(1)).findById(2L);

        reset(publishedPostRepository);

    }

    @Test
    public void testPermalinkByIdCachedOnFirstCall() {

        // Given

        cacheManager.getCache(CACHE_POST_PERMALINK_BY_ID).clear();
        when(publishedPostRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        when(publishedPostRepository.getPermalinkById(anyLong())).thenReturn(any());

        // When

        publishPostService.getPermalink(1L);
        publishPostService.getPermalink(1L);
        publishPostService.getPermalink(2L);
        publishPostService.getPermalink(2L);

        // Then

        assertNotNull(cacheManager.getCache(CACHE_POST_PERMALINK_BY_ID));
        verify(publishedPostRepository, times(1)).getPermalinkById(1L);
        verify(publishedPostRepository, times(1)).getPermalinkById(2L);

        reset(publishedPostRepository);

    }

    @Test
    public void testEvictPermalinkByIdCacheOnCreateUpdatePost() {

        // Given

        cacheManager.getCache(CACHE_POST_PERMALINK_BY_ID).clear();
        when(publishedPostRepository.findById(anyLong())).thenReturn(Optional.of(buildPublishedPost()));
        when(publishedPostRepository.save(any())).thenReturn(buildPublishedPost());
        when(publishedPostRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        when(publishedPostRepository.getPermalinkById(anyLong())).thenReturn(any());

        // When

        publishPostService.getPermalink(1L);
        publishPostService.getPermalink(2L);
        publishPostService.createUpdatePost(buildPublishedPostDTO());
        publishPostService.getPermalink(1L);
        publishPostService.getPermalink(2L);

        // Then

        assertNotNull(cacheManager.getCache(CACHE_POST_PERMALINK_BY_ID));
        verify(publishedPostRepository, times(2)).getPermalinkById(1L);
        verify(publishedPostRepository, times(1)).getPermalinkById(2L);

        reset(publishedPostRepository);

    }

    @Test
    public void testPostPageCachedOnFirstCall() {

        // Given

        cacheManager.getCache(CACHE_POSTS_PAGE).clear();

        Pageable firstPage = PageRequest.of(0, 10);
        when(publishedPostRepository.findAll(firstPage)).thenReturn(
                new PageImpl<>(Collections.singletonList(buildPublishedPost()), firstPage, 1));

        Pageable secondPage = PageRequest.of(1, 10);
        when(publishedPostRepository.findAll(secondPage)).thenReturn(
                new PageImpl<>(Collections.singletonList(buildPublishedPost()), secondPage, 1));

        // When

        publishPostService.getPosts(firstPage);
        publishPostService.getPosts(firstPage);
        publishPostService.getPosts(secondPage);
        publishPostService.getPosts(secondPage);

        // Then

        assertNotNull(cacheManager.getCache(CACHE_POSTS_PAGE));
        verify(publishedPostRepository, times(1)).findAll(firstPage);
        verify(publishedPostRepository, times(1)).findAll(secondPage);

        reset(publishedPostRepository);

    }

    @Test
    public void testEvictPostPageCacheOnCreateUpdatePost() {

        // Given

        cacheManager.getCache(CACHE_POSTS_PAGE).clear();
        when(publishedPostRepository.findById(anyLong())).thenReturn(Optional.of(buildPublishedPost()));
        when(publishedPostRepository.save(any())).thenReturn(buildPublishedPost());

        when(publishedPostRepository.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(Collections.singletonList(buildPublishedPost()), PageRequest.of(0, 10), 1));

        // When

        publishPostService.getPosts(PageRequest.of(0, 10));
        publishPostService.getPosts(PageRequest.of(1, 10));
        publishPostService.createUpdatePost(buildPublishedPostDTO());
        publishPostService.getPosts(PageRequest.of(0, 10));
        publishPostService.getPosts(PageRequest.of(1, 10));

        // Then

        assertNotNull(cacheManager.getCache(CACHE_POSTS_PAGE));
        verify(publishedPostRepository, times(2)).findAll(PageRequest.of(0, 10));
        verify(publishedPostRepository, times(2)).findAll(PageRequest.of(1, 10));

        reset(publishedPostRepository);

    }

    private static PublishedPost buildPublishedPost() {

        PublishedPost post = new PublishedPost();
        post.setId(1L);
        post.setTitle("SamplePost");
        post.setContent("Hello world!");
        post.setPermalink("sample-post");

        return post;
    }

    private static PostDTO buildPublishedPostDTO() {

        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("SamplePost");
        postDTO.setContent("Hello world!");
        postDTO.setPermalink("sample-post");
        postDTO.setStatus(CurrentPostStatus.PUBLISHED);

        return postDTO;
    }

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        public PublishPostService publishPostService() {
            return new PublishPostServiceImpl(categoryService(), tagService(), publishedPostRepository(),
                    draftPostRepository(), dateUtility(), modelMapper());
        }

        @Bean
        public CategoryService categoryService() {
            return mock(CategoryServiceImpl.class);
        }

        @Bean
        public TagService tagService() {
            return mock(TagService.class);
        }

        @Bean
        public PublishedPostRepository publishedPostRepository() {
            return mock(PublishedPostRepository.class);
        }

        @Bean
        public DraftPostRepository draftPostRepository() {
            return mock(DraftPostRepository.class);
        }

        @Bean
        public DateUtil dateUtility() {
            return mock(DateUtil.class);
        }

        @Bean
        public ModelMapper modelMapper() {
            return new MappingConfig().createModelMapper();
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }

    }
}
