package com.unloadbrain.blog.controller.admin;

import com.unloadbrain.blog.dto.CategoryDTO;
import com.unloadbrain.blog.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping("/admin/categories")
    public String loadCategoryUI() {
        return "admin/categories";
    }

    @PostMapping("/admin/categories")
    public String createUpdateCategories(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.createUpdateCategory(categoryDTO);
        return "redirect:/admin/categories";
    }
}
