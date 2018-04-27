package com.unloadbrain.blog.controller.admin;

import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/admin/post")
    public String showPostCreateEditPage() {
        return "admin/post";
    }

    @PostMapping("/admin/post")
    public String createPost(@ModelAttribute PostDTO postDTO) {
        postService.createPost(postDTO);
        return "redirect:/admin/post";
    }
}
