package com.main.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/q1")
    public String q1() {
        return "q1";
    }

    @GetMapping("/q2")
    public String q2() {
        return "q2";
    }

    @GetMapping("/q3")
    public String q3() {
        return "q3";
    }

    @GetMapping("/q4")
    public String q4() {
        return "q4";
    }

    @GetMapping("/rekomendasi")
    public String rekomendasi() {
        return "rekomendasi";
    }

    @GetMapping("/collection")
    public String collection() {
        return "collection";
    }

    @GetMapping("/detail")
    public String showDetailPage() {
        return "detail";
    }
}
