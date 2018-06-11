package com.unloadbrain.blog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

    @RequestMapping(path = "/admin/dashboard", method = RequestMethod.GET)
    public ModelAndView showDashboard() {
        return new ModelAndView("admin/dashboard");
    }
}
