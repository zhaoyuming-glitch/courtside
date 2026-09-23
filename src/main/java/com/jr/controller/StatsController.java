package com.jr.controller;

import com.jr.dto.players.Player;
import com.jr.dto.players.SeasonAvgDTO;
import com.jr.dto.stats.PlayerStat;
import com.jr.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/stats")
public class StatsController {
    @Autowired
    private StatsService statsService;
    @RequestMapping("/homeTeamStats")
    @ResponseBody
    public List<PlayerStat>getHomeTeamStats(@RequestParam Integer gameId){
    return statsService.getHomeTeamStatsByScore(gameId);
    }
    @RequestMapping("/visitorTeamStats")
    @ResponseBody
    public List<PlayerStat>getVisitorTeamStats(@RequestParam Integer gameId) {
        return statsService.getVisitorTeamStatsByScore(gameId);
    }
    @RequestMapping("/homeTeamTotals")
    @ResponseBody
    public Map<String,Object>getHomeTeamTotals(@RequestParam Integer gameId){
        return statsService.getHomeTeamTotals(gameId);
    }
    @RequestMapping("/visitorTeamTotals")
    @ResponseBody
    public Map<String,Object>getVisitorTeamTotals(@RequestParam Integer gameId){
        return statsService.getVisitorTeamTotals(gameId);
    }
    @RequestMapping("/PlayerSingleGameStats")
    @ResponseBody
    public PlayerStat getPlayerSingleGameStats(@RequestParam Integer playerId,
                                               @RequestParam Integer gameId) {
        return statsService.getPlayerSingleGameStats(playerId, gameId);
    }
    @RequestMapping("/SeasonAvg")
    @ResponseBody
    public SeasonAvgDTO getSeasonAvg(@RequestParam Integer playerId,
                                     @RequestParam Integer season) {
        return statsService.getSeasonAvg(playerId, season);
    }
}

