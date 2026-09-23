package com.jr.controller;

import com.jr.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestParam("account") String account,
                           @RequestParam("password") String password,
                           @RequestParam("username") String username) {

        int result = authService.register(username, account, password);

        if (result == 1) {
            return "redirect:/register.html?error=account_exists";
        }
        return "redirect:/login.html?registered=true";
    }
}