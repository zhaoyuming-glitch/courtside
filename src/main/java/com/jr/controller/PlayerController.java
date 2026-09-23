package com.jr.controller;

import com.jr.dto.players.Player;
import com.jr.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping("/{playerId}")
    @ResponseBody
    public Player getPlayerById(@PathVariable("playerId") int playerId) {
        return playerService.getPlayer(playerId);
    }
}