package com.unloadbrain.blog.controller;

import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.service.CategoryService;
import com.unloadbrain.blog.service.PublishPostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@AllArgsConstructor
@Controller
public class SitePostController {

    private PublishPostService publishPostService;
    private CategoryService categoryService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String showHomePage(Pageable pageable, Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("tags", new ArrayList<String>());
        model.addAttribute("postsPage", publishPostService.getPublishedPosts(pageable));
        return "site/home/main";
    }

    @GetMapping(path = "/posts/{id}")
    public String redirectWithPostPermalink(@PathVariable long id, Model model) {
        return String.format("redirect:/posts/%d/%s", id, publishPostService.getPublishedPostPermalink(id));

    }

    @GetMapping(path = "/posts/{id}/{permalink}")
    public String showPostPage(@PathVariable long id, @PathVariable String permalink, Model model) {

        String postPermalink = publishPostService.getPublishedPostPermalink(id);
        if (!postPermalink.equals(permalink)) {
            return String.format("redirect:/posts/%d/%s", id, postPermalink);
        }

        PostDTO postDTO = publishPostService.getPublishedPost(id);
        model.addAttribute("post", postDTO);
        return "site/post/main";
    }

}
