package com.unloadbrain.blog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController {

    @RequestMapping(path = "/admin/post", method = RequestMethod.GET)
    public String showPostCreateEditPage() {
        return "admin/post";
    }

    @RequestMapping(path = "/admin/post", method = RequestMethod.POST)
    public String createPost() {
        return "redirect:/admin/post";
    }
}
