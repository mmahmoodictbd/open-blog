package com.unloadbrain.blog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CategoryController {

    @RequestMapping(path = "/admin/category", method = RequestMethod.GET)
    public String loadCategoryUI() {
        return "admin/category";
    }
}
