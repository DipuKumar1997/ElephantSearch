package com.io.search_engine;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")//or add the index.jsp use thymeleaf
    public String home() {
        return "index"; //  pointing to /WEB-INF/views/index.jsp
    }
}