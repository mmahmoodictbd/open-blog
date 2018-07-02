package com.unloadbrain.blog.controller.site;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class PingController {

    @GetMapping(value = "/ping")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ping() {
    }
}
