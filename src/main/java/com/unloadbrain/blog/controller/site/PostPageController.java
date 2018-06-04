package com.unloadbrain.blog.controller.site;

import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.service.PublishPostService;
import com.unloadbrain.blog.service.SiteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@Controller
public class PostPageController {

    private SiteService siteService;
    private PublishPostService publishPostService;

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

        model.addAttribute("site", siteService.getSite());

        PostDTO postDTO = publishPostService.getPublishedPost(id);
        model.addAttribute("post", postDTO);
        return "site/post/main";
    }

}
