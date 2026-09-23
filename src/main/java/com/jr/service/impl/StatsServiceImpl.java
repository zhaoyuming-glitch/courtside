package com.jr.service.impl;

import com.jr.client.StatsClient;
import com.jr.dto.players.SeasonAvgDTO;
import com.jr.dto.stats.PlayerStat;
import com.jr.enums.TeamType;
import com.jr.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private StatsClient client;

    @Override
    public List<PlayerStat> getHomeTeamStatsByScore(Integer gameId) {
        return this.getTeamStatsByScore(gameId, TeamType.HOME);
    }

    @Override
    public List<PlayerStat> getVisitorTeamStatsByScore(Integer gameId) {
        return this.getTeamStatsByScore(gameId, TeamType.VISITOR);
    }

    @Override
    public Map<String, Object> getHomeTeamTotals(Integer gameId) {
        return this.getTeamTotals(gameId, TeamType.HOME);
    }

    @Override
    public Map<String, Object> getVisitorTeamTotals(Integer gameId) {
        return this.getTeamTotals(gameId, TeamType.VISITOR);
    }

    @Override
    public PlayerStat getPlayerSingleGameStats(Integer playerId, Integer gameId) {
        Map<String, String> params = new HashMap<>();
        params.put("game_ids[]", String.valueOf(gameId));
        List<PlayerStat> stats = client.getStats(params);

        if (stats == null || stats.isEmpty()) {
            return null;
        }

        for (PlayerStat stat : stats) {
            if (stat.getPlayer() != null && stat.getPlayer().getId().equals(playerId)) {
                return stat;
            }
        }
        return null;
    }

    @Override
    public SeasonAvgDTO getSeasonAvg(Integer playerId, Integer season) {
        HashMap<String, String> map = new HashMap<>();
        map.put("player_ids[]", String.valueOf(playerId));
        map.put("seasons[]", String.valueOf(season));
        List<PlayerStat> stats = client.getStats(map);
        if (stats == null || stats.isEmpty()) {
            return new SeasonAvgDTO();
        }
        double points = 0;
        double rebounds = 0;
        double assists = 0;
        int game = 0;
        for (PlayerStat s : stats) {
            boolean hasValidData = false;
            if (s.getPts() != null) {
                points += s.getPts();
                hasValidData = true;
            }
            if (s.getReb() != null) {
                rebounds += s.getReb();
                hasValidData = true;
            }
            if (s.getAst() != null) {
                assists += s.getAst();
                hasValidData = true;
            }
            if (hasValidData) {
                game++;
            }
        }
        if (game == 0) {
            return new SeasonAvgDTO();
        }
        return new SeasonAvgDTO(points / game, rebounds / game, assists / game);
    }

    private List<PlayerStat> getTeamStatsByScore(Integer gameId, TeamType type) {
        List<PlayerStat> stats = client.getStatsByGame(gameId);
        if (stats == null || stats.isEmpty()) {
            return Collections.emptyList();
        }

        List<PlayerStat> result = new ArrayList<>();
        for (PlayerStat ps : stats) {
            if (ps == null || ps.getTeam() == null || ps.getGame() == null) {
                continue;
            }
            if (isMatch(type, ps)) {
                result.add(ps);
            }
        }

        result.sort((p1, p2) -> {
            int pts1 = p1.getPts() != null ? p1.getPts() : 0;
            int pts2 = p2.getPts() != null ? p2.getPts() : 0;
            return Integer.compare(pts2, pts1);
        });

        return result;
    }

    private static boolean isMatch(TeamType type, PlayerStat ps) {  // ✅ 改这里
        Integer teamId = ps.getTeam().getId();
        Integer homeId = ps.getGame().getHome_team_id();
        Integer visitorId = ps.getGame().getVisitor_team_id();

        if (type == TeamType.HOME) {
            if (teamId != null && homeId != null && teamId.equals(homeId)) {
                return true;
            }
        } else {
            if (teamId != null && visitorId != null && teamId.equals(visitorId)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> getTeamTotals(Integer gameId, TeamType type) {
        List<PlayerStat> stats = this.getTeamStatsByScore(gameId, type);
        if (stats.isEmpty()) return Collections.emptyMap();
        return this.calcTeamTotals(stats);
    }

    private Map<String, Object> calcTeamTotals(List<PlayerStat> stats) {
        if (stats == null || stats.isEmpty()) return Collections.emptyMap();

        int fgm = 0, fga = 0;
        int fg3m = 0, fg3a = 0;
        int ftm = 0, fta = 0;
        int reb = 0, ast = 0, stl = 0, blk = 0, turnover = 0, pf = 0;

        for (PlayerStat s : stats) {
            fgm += s.getFgm() != null ? s.getFgm() : 0;
            fga += s.getFga() != null ? s.getFga() : 0;
            fg3m += s.getFg3m() != null ? s.getFg3m() : 0;
            fg3a += s.getFg3a() != null ? s.getFg3a() : 0;
            ftm += s.getFtm() != null ? s.getFtm() : 0;
            fta += s.getFta() != null ? s.getFta() : 0;
            reb += s.getReb() != null ? s.getReb() : 0;
            ast += s.getAst() != null ? s.getAst() : 0;
            stl += s.getStl() != null ? s.getStl() : 0;
            blk += s.getBlk() != null ? s.getBlk() : 0;
            turnover += s.getTurnover() != null ? s.getTurnover() : 0;
            pf += s.getPf() != null ? s.getPf() : 0;
        }

        String fgPct = fga > 0 ? String.format("%.1f", (fgm * 100.0 / fga)) + "%" : "0%";
        String fg3Pct = fg3a > 0 ? String.format("%.1f", (fg3m * 100.0 / fg3a)) + "%" : "0%";
        String ftPct = fta > 0 ? String.format("%.1f", (ftm * 100.0 / fta)) + "%" : "0%";

        Map<String, Object> totals = new HashMap<>();
        totals.put("fg_pct", fgPct);
        totals.put("fg3_pct", fg3Pct);
        totals.put("ft_pct", ftPct);
        totals.put("reb", reb);
        totals.put("ast", ast);
        totals.put("stl", stl);
        totals.put("blk", blk);
        totals.put("turnover", turnover);
        totals.put("pf", pf);
        return totals;
    }
}