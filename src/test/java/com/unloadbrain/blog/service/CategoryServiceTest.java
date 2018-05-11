package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.dto.CategoryDTO;
import com.unloadbrain.blog.dto.IdentityDTO;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {

    private CategoryRepository categoryRepositoryMock;
    private CategoryService categoryService;

    public CategoryServiceTest() {
        this.categoryRepositoryMock = mock(CategoryRepository.class);
        this.categoryService = new CategoryService(categoryRepositoryMock);
    }


    @Test
    public void testCreateCategory() {

        // Given

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        Category category = new Category();
        category.setId(1L);
        category.setName("Hello World");
        when(categoryRepositoryMock.save(any())).thenReturn(category);

        CategoryDTO categoryDTO = new CategoryDTO(null, "Hello World", null, null);

        // When
        IdentityDTO identityDTO = categoryService.createUpdateCategory(categoryDTO);

        // Then

        assertNotNull("Should have id of created category.", identityDTO.getId());

        verify(categoryRepositoryMock).save(categoryArgumentCaptor.capture());
        assertEquals("hello-world", categoryArgumentCaptor.getValue().getSlug());

    }

    @Test
    public void testUpdateCategory() {

        // Given

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        Category persistedCategory = new Category();
        persistedCategory.setId(1L);
        persistedCategory.setName("Hello World");
        persistedCategory.setSlug("hello-world");
        when(categoryRepositoryMock.getOne(any())).thenReturn(persistedCategory);


        Category category = new Category();
        category.setId(1L);
        category.setName("Hello World1");
        category.setSlug("hello");
        when(categoryRepositoryMock.save(any())).thenReturn(category);

        CategoryDTO categoryDTO = new CategoryDTO(1L, "Hello World1", "hello", null);

        // When
        IdentityDTO identityDTO = categoryService.createUpdateCategory(categoryDTO);

        // Then

        assertNotNull("Should have id of created category.", identityDTO.getId());

        verify(categoryRepositoryMock).save(categoryArgumentCaptor.capture());
        assertEquals("Hello World1", categoryArgumentCaptor.getValue().getName());
        assertEquals("hello", categoryArgumentCaptor.getValue().getSlug());

    }

}