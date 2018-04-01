package com.unloadbrain.blog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class PostController {

    @RequestMapping(path = "/admin/post", method = RequestMethod.GET)
    public String postForm() {
        return "admin/post";
    }
}
