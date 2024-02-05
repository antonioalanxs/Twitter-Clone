package com.TwitterClone.ProjectBackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SPAController {
    @GetMapping({"/new/**/{path:[^\\.]*}", "/{path:new[^\\.]*}"})
    public String redirect() {
        return "forward:/new/index.html";
    }
}
