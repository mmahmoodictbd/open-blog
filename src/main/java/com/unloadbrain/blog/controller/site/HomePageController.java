package com.unloadbrain.blog.controller.site;

import com.unloadbrain.blog.service.CategoryService;
import com.unloadbrain.blog.service.PublishPostService;
import com.unloadbrain.blog.service.SiteService;
import com.unloadbrain.blog.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@AllArgsConstructor
@Controller
public class HomePageController {

    private SiteService siteService;
    private PublishPostService publishPostService;
    private CategoryService categoryService;
    private TagService tagService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String showHomePage(Pageable pageable, Model model) {

        model.addAttribute("site", siteService.getSite());
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("tags", tagService.getTags());
        model.addAttribute("postsPage", publishPostService.getPublishedPosts(pageable));

        return "site/home/main";
    }

}
