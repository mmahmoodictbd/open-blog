package com.unloadbrain.blog.controller.admin;

import com.unloadbrain.blog.converter.PostStatusConverter;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.dto.PostStatusDTO;
import com.unloadbrain.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(PostStatusDTO.class, new PostStatusConverter());
    }

    @GetMapping("/admin/post")
    public String showPostCreateEditPage(@RequestParam(required = false) Long id,
                                         @RequestParam(required = false)  PostStatusDTO status) {
        return "admin/post";
    }

    @PostMapping("/admin/post")
    public String createPost(@ModelAttribute PostDTO postDTO) {

        PostIdentityDTO postIdentityDTO = postService.createUpdatePost(postDTO);
        return String.format("redirect:/admin/post?id=%d&status=%s",
                postIdentityDTO.getId(), postIdentityDTO.getStatus());
    }
}
