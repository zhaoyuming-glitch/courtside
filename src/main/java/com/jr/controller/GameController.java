package com.jr.controller;

import com.jr.dto.games.Game;
import com.jr.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping("/scoreboard")
    @ResponseBody
    public List<Game> showScoreBoard() {
        return gameService.getScoreBoardGame();
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public Game getGameById(@PathVariable("id") int gameId) {
        return gameService.getGameById(gameId);
    }

    @RequestMapping("/date")
    @ResponseBody
    public List<Game> getByDate(@RequestParam("date") String date) {
        return gameService.getGamesByDate(date);
    }
}