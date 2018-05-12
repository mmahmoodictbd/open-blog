package com.unloadbrain.blog.controller.admin;

import com.unloadbrain.blog.dto.CategoryDTO;
import com.unloadbrain.blog.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping("/admin/categories")
    public String loadCategoryUI(@RequestParam(required = false) Long id, Pageable pageable, Model model) {

        if (id != null) {
            model.addAttribute("category", categoryService.getCategory(id));
        }

        model.addAttribute("categories", categoryService.getCategories(pageable).getContent());
        return "admin/categories";
    }

    @PostMapping("/admin/categories")
    public String createUpdateCategories(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.createUpdateCategory(categoryDTO);
        return "redirect:/admin/categories";
    }
}
