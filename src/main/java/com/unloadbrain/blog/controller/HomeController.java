package com.unloadbrain.blog.controller;

import com.unloadbrain.blog.service.CategoryService;
import com.unloadbrain.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@AllArgsConstructor
@Controller
public class HomeController {

    private PostService postService;
    private CategoryService categoryService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String showHomePage(Pageable pageable, Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("tags", new ArrayList<String>());
        model.addAttribute("postsPage", postService.getPublishedPosts(pageable));
        return "site/home/main";
    }

}
