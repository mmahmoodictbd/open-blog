package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.dto.CategoryDTO;
import com.unloadbrain.blog.dto.IdentityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    IdentityDTO createUpdateCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getCategories();

    Page<CategoryDTO> getCategories(Pageable pageable);

    CategoryDTO getCategory(Long id);

    Set<Category> getCategories(String postCategories);
}
