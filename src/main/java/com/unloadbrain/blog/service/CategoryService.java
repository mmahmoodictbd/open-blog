package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.dto.CategoryDTO;
import com.unloadbrain.blog.dto.IdentityDTO;
import com.unloadbrain.blog.util.SlugUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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

        if (category.getName()!= null && !categoryDTO.getName().equals(category.getName())) {
            List<Category> categoriesUsingThisCategoryAsParent =
                    categoryRepository.findCategoriesByParent(category.getName());
            for(Category cat : categoriesUsingThisCategoryAsParent) {
                cat.setParent(categoryDTO.getName());
                categoryRepository.save(cat);
            }
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


    public Page<CategoryDTO> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::convertToCategoryDTO);
    }

    private CategoryDTO convertToCategoryDTO(final Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getSlug(), category.getParent());
    }

    public CategoryDTO getCategory(Long id) {

        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            return convertToCategoryDTO(categoryOptional.get());
        } else {
            // TODO:: Throw proper custom exception
            throw new IllegalArgumentException("Invalid category id");
        }

    }
}
