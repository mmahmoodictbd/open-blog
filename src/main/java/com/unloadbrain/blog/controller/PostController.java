package com.unloadbrain.blog.controller;

import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@Controller
public class PostController {

    private PostService postService;

    @GetMapping(path = "/posts/{id}")
    public String redirectWithPostPermalink(@PathVariable long id, Model model) {
        return String.format("redirect:/posts/%d/%s", id, postService.getPublishedPostPermalink(id));

    }

    @GetMapping(path = "/posts/{id}/{permalink}")
    public String showPostPage(@PathVariable long id, @PathVariable String permalink, Model model) {

        String postPermalink = postService.getPublishedPostPermalink(id);
        if (!postPermalink.equals(permalink)) {
            return String.format("redirect:/posts/%d/%s", id, postPermalink);
        }

        PostDTO postDTO = postService.getPublishedPost(id);
        model.addAttribute("post", postDTO);
        return "site/post/main";
    }

}
