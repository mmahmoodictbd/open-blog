package com.unloadbrain.blog.controller.admin;

import com.unloadbrain.blog.converter.PostStatusConverter;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.dto.PostStatusDTO;
import com.unloadbrain.blog.service.CategoryService;
import com.unloadbrain.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class PostController {

    private PostService postService;
    private CategoryService categoryService;

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(PostStatusDTO.class, new PostStatusConverter());
    }

    @GetMapping("/admin/posts")
    public String showPostListPage(Model model, Pageable pageable) {

        model.addAttribute("posts", postService.getPosts(pageable).getContent());

        return "admin/post-list";
    }

    @GetMapping("/admin/post")
    public String showPostCreateEditPage(@RequestParam(required = false) Long id,
                                         @RequestParam(required = false) PostStatusDTO status,
                                         Model model) {

        if (id != null && status != null) {
            model.addAttribute("post", postService.getPost(id, status));
        }

        model.addAttribute("categories", categoryService.getCategories());

        return "admin/post";
    }

    @PostMapping("/admin/post")
    public String createPost(@ModelAttribute PostDTO postDTO) {

        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);
        return String.format("redirect:/admin/post?id=%d&status=%s",
                postIdentityDTO.getId(), postIdentityDTO.getStatus());
    }
}
