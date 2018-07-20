package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.TagRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("it")
public class TagServiceIT {

    private static final String CACHE_KEY_TAGS = "tags";

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private CacheManager cacheManager;

    // FIXME:: Test in working, fix getTags() in TagServiceImpl first.
    @Ignore
    @Test
    public void testTagsCachedOnFirstCall() {

        // Given

        cacheManager.getCache(CACHE_KEY_TAGS).clear();
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(buildTag()));

        // When

        tagService.getTags();
        tagService.getTags();

        // Then

        assertNotNull(cacheManager.getCache(CACHE_KEY_TAGS));
        verify(tagRepository, times(1)).findAll();

        reset(tagRepository);

    }

    private Tag buildTag() {
        Tag tag = new Tag();
        tag.setName("tag1");
        tag.setSlug("tag1");
        return tag;
    }

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        public TagService tagService() {
            return new TagServiceImpl(tagRepository());
        }

        @Bean
        public TagRepository tagRepository() {
            return mock(TagRepository.class);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }

    }
}
