package com.jr.controller;

import com.jr.service.RecapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courtside/recap")
public class RecapController {

    @Autowired
    private RecapService recapService;

    @RequestMapping ("/{gameId}")
    public String getRecap(@PathVariable int gameId) {
        return recapService.getRecap(gameId);
    }

    @RequestMapping("/regenerate/{gameId}")
    public String regenerate(@PathVariable int gameId) {
        return recapService.regenerate(gameId);
    }
}