package com.searchengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/home")
    public String search(){
        return "search";
    }

    @GetMapping("/uploadFile")
    public String upload(){
        return "upload";
    }

}
