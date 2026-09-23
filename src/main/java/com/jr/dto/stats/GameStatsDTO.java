package com.jr.dto.stats;

import com.jr.dto.common.Team;

import java.util.List;
import java.util.Map;


public class GameStatsDTO {
    private Team homeTeam;
    private List<PlayerStat> homePlayers;
    private Team visitorTeam;
    private List<PlayerStat> visitorPlayers;
    private Map<String, Object> totals;
}
