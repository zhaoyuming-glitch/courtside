package com.jr.service;

import com.jr.dto.players.SeasonAvgDTO;
import com.jr.dto.stats.PlayerStat;
import java.util.List;
import java.util.Map;

public interface StatsService {
    List<PlayerStat> getHomeTeamStatsByScore(Integer gameId);
    List<PlayerStat> getVisitorTeamStatsByScore(Integer gameId);
    Map<String, Object> getHomeTeamTotals(Integer gameId);
    Map<String, Object> getVisitorTeamTotals(Integer gameId);
    PlayerStat getPlayerSingleGameStats(Integer playerId, Integer gameId);
    SeasonAvgDTO getSeasonAvg(Integer playerId, Integer season);
}