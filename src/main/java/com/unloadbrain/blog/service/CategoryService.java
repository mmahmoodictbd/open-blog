package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.dto.CategoryDTO;
import com.unloadbrain.blog.dto.IdentityDTO;
import com.unloadbrain.blog.exception.InvalidPostCategoryException;
import com.unloadbrain.blog.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryService {

    private static final String COMMA = ",";

    private CategoryRepository categoryRepository;

    @Transactional
    public IdentityDTO createUpdateCategory(CategoryDTO categoryDTO) {

        Category category;

        if (categoryDTO.getId() != null) {
            category = categoryRepository.getOne(categoryDTO.getId());
        } else {
            category = new Category();
        }

        if (category.getName() != null && !categoryDTO.getName().equals(category.getName())) {
            List<Category> categoriesUsingThisCategoryAsParent =
                    categoryRepository.findCategoriesByParent(category.getName());
            for (Category cat : categoriesUsingThisCategoryAsParent) {
                cat.setParent(categoryDTO.getName());
                categoryRepository.save(cat);
            }
        }
        category.setName(categoryDTO.getName());

        if (categoryDTO.getSlug() == null || categoryDTO.getSlug().trim().length() == 0) {
            category.setSlug(SlugUtil.toSlug(categoryDTO.getName()));
        } else {
            category.setSlug(categoryDTO.getSlug());
        }

        category.setParent(categoryDTO.getParent());

        category = categoryRepository.save(category);

        return new IdentityDTO(category.getId());
    }

    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAll().stream().map(this::convertToCategoryDTO).collect(Collectors.toList());
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

    public Set<Category> getCategories(String postCategories) {

        Set<Category> categories = new LinkedHashSet<>();

        if (postCategories == null || postCategories.trim().length() == 0) {
            return categories;
        }

        for (String categoryString : postCategories.split(COMMA)) {
            Category persistedCategory = categoryRepository.findByName(categoryString);
            if (persistedCategory == null) {
                throw new InvalidPostCategoryException(
                        String.format("Could not found category: %s in database.", categoryString));
            }
            categories.add(persistedCategory);
        }

        return categories;
    }
}
