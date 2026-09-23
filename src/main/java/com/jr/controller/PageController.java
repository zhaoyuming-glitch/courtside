package com.jr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/index")
    public String indexPage() { return "index"; }

    @GetMapping("/login.html")
    public String login() { return "login"; }

    @GetMapping("/register.html")
    public String register() { return "register"; }

    @GetMapping("/gamedetails")
    public String gamedetails() { return "gamedetails"; }

    @GetMapping("/playeringame")
    public String playeringame() { return "playeringame"; }
    @GetMapping("/schedule")
    public String schedule() {
        return "schedule";
    }
}