package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.dto.CategoryDTO;
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
public class CategoryServiceIT {

    private static final String CACHE_KEY_CATEGORIES = "categories";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCategoriesCachedOnFirstCall() {

        // Given

        cacheManager.getCache(CACHE_KEY_CATEGORIES).clear();
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(buildCategoryForCreate()));

        // When

        categoryService.getCategories();
        categoryService.getCategories();

        // Then
        assertNotNull(cacheManager.getCache(CACHE_KEY_CATEGORIES));
        verify(categoryRepository, times(1)).findAll();

        reset(categoryRepository);

    }


    @Test
    public void shouldEvictCacheOnCreateUpdateCategory() {

        // Given

        cacheManager.getCache(CACHE_KEY_CATEGORIES).clear();
        when(categoryRepository.getOne(anyLong())).thenReturn(buildCategoryForUpdate());
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(buildCategoryForUpdate()));
        when(categoryRepository.save(any())).thenReturn(buildCategoryForUpdate());

        // When

        categoryService.getCategories();
        categoryService.createUpdateCategory(buildCategoryDTOForUpdate());
        categoryService.getCategories();

        // Then

        assertNotNull(cacheManager.getCache(CACHE_KEY_CATEGORIES));
        verify(categoryRepository, times(2)).findAll();

        reset(categoryRepository);

    }

    private Category buildCategoryForCreate() {
        Category category = new Category();
        category.setName("category1");
        category.setSlug("category1");
        return category;
    }

    private Category buildCategoryForUpdate() {
        Category category = new Category();
        category.setId(1L);
        category.setName("category1");
        category.setSlug("category1");
        return category;
    }

    private CategoryDTO buildCategoryDTOForCreate() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("category1");
        categoryDTO.setSlug("category1");
        return categoryDTO;
    }

    private CategoryDTO buildCategoryDTOForUpdate() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("category2");
        categoryDTO.setSlug("category2");
        return categoryDTO;
    }

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        public CategoryService categoryService() {
            return new CategoryServiceImpl(categoryRepository());
        }

        @Bean
        public CategoryRepository categoryRepository() {
            return mock(CategoryRepository.class);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }

    }
}
