package com.unloadbrain.blog.controller.admin;

import com.unloadbrain.blog.dto.SiteUpdateCommandDTO;
import com.unloadbrain.blog.service.SiteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class SiteController {

    private SiteService siteService;

    @GetMapping("/admin/site")
    public String showEditSitePage(Model model) {
        model.addAttribute("site", siteService.getSite());
        return "admin/site";
    }

    @PostMapping("/admin/site")
    public String updateSite(@ModelAttribute SiteUpdateCommandDTO siteUpdateCommandDTO) {
        siteService.updateSite(siteUpdateCommandDTO);
        return "redirect:/admin/site";
    }
}
