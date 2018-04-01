package com.unloadbrain.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model) {
        List<String> posts = new ArrayList<>();
        posts.add("1");
        posts.add("2");
        posts.add("3");
        posts.add("4");

        model.addAttribute("posts", posts);
        return "site/home/main";
    }

    @RequestMapping(path = "/post", method = RequestMethod.GET)
    public String post(@RequestParam long id, Model model) {
        model.addAttribute("posts", null);
        return "site/post/main";
    }

}
