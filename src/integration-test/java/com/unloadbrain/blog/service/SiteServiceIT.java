package com.unloadbrain.blog.service;

import com.unloadbrain.blog.config.MappingConfig;
import com.unloadbrain.blog.domain.model.Site;
import com.unloadbrain.blog.domain.repository.SiteRepository;
import com.unloadbrain.blog.dto.SiteUpdateCommandDTO;
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
public class SiteServiceIT {

    private static final String CACHE_KEY_SITE = "site";

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteService siteService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testSiteCachedOnFirstCall() {

        // Given

        cacheManager.getCache(CACHE_KEY_SITE).clear();
        when(siteRepository.findAll()).thenReturn(Collections.singletonList(buildSite()));

        // When

        siteService.getSite();
        siteService.getSite();

        // Then

        assertNotNull(cacheManager.getCache(CACHE_KEY_SITE));
        verify(siteRepository, times(1)).findAll();

        reset(siteRepository);

    }

    @Test
    public void testSiteCacheEvictionOnUpdateSite() {

        // Given

        cacheManager.getCache(CACHE_KEY_SITE).clear();
        when(siteRepository.findAll()).thenReturn(Collections.singletonList(buildSite()));

        // When

        // siteRepository.findAll() get called once here
        siteService.getSite();

        // siteRepository.findAll() get called once here
        siteService.updateSite(buildSiteUpdateCommandDTO());

        // siteRepository.findAll() get called once here because cache got evicted
        siteService.getSite();

        // Then

        assertNotNull(cacheManager.getCache(CACHE_KEY_SITE));
        verify(siteRepository, times(3)).findAll();

        reset(siteRepository);

    }

    private Site buildSite() {
        Site site = new Site();
        site.setName("site1");
        site.setDescription("site1");
        site.setSiteUrl("localhost:8080");
        site.setHomeUrl("localhost:8080");
        return site;
    }

    private SiteUpdateCommandDTO buildSiteUpdateCommandDTO() {
        SiteUpdateCommandDTO siteUpdateCommandDTO = new SiteUpdateCommandDTO();
        siteUpdateCommandDTO.setName("site2");
        siteUpdateCommandDTO.setDescription("site2");
        siteUpdateCommandDTO.setSiteUrl("localhost:8082");
        siteUpdateCommandDTO.setHomeUrl("localhost:8082");
        return siteUpdateCommandDTO;
    }

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        public SiteService siteService() {
            return new SiteServiceImpl(siteRepository(), modelMapper());
        }

        @Bean
        public SiteRepository siteRepository() {
            return mock(SiteRepository.class);
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
