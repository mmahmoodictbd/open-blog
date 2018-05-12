package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.dto.CategoryDTO;
import com.unloadbrain.blog.dto.IdentityDTO;
import com.unloadbrain.blog.util.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public IdentityDTO createUpdateCategory(CategoryDTO categoryDTO) {

        Category category;

        if (categoryDTO.getId() != null) {
            category = categoryRepository.getOne(categoryDTO.getId());
        } else {
            category = new Category();
        }

        category.setName(categoryDTO.getName());

        if (categoryDTO.getSlug() == null || categoryDTO.getSlug().trim().length() == 0)  {
            category.setSlug(SlugUtil.toSlug(categoryDTO.getName()));
        } else {
            category.setSlug(categoryDTO.getSlug());
        }

        category.setParent(categoryDTO.getParent());

        category = categoryRepository.save(category);

        return new IdentityDTO(category.getId());
    }


}
