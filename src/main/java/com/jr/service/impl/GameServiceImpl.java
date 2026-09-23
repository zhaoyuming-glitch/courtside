package com.jr.service.impl;

import com.jr.client.GameClient;
import com.jr.dto.games.Game;
import com.jr.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameClient gameClient;
    private final int lookBackDays = 3;
    private final int needGame = 4;
    @Override
    public List<Game> getScoreBoardGame() {
        String date=LocalDate.now().toString();
        List<Game> toDayGames = this.getGamesByDate(date);
        if (!toDayGames.isEmpty()){
           return this.sortTodayGames(toDayGames).stream()
                    .limit(needGame)
                    .toList();
        }
        List<Game> result = new ArrayList<>();
        for (int i=1;i<=lookBackDays;i++){
            if (result.size()>=needGame)break;
            List<Game> gamesByDate = this.getGamesByDate(LocalDate.now().minusDays(i).toString());
            for (int j = 0; j <needGame&&j<gamesByDate.size() ; j++) {
                result.add(gamesByDate.get(j));
            }
        }
        return result;
    }

    @Override
    public Game getGameById(int gameId) {
        return gameClient.getGameById(gameId);
    }

    @Override
    public List<Game> getGamesByDate(String date) {
        HashMap<String, String> param = new HashMap<>();
        param.put("dates[]",date);
        return gameClient.getGames(param);

    }

    @Override
    public List<Game> getGamesByDateRange(String startDate, String endDate) {
        return List.of();
    }
    private List<Game> sortTodayGames(List<Game> games) {
        games.sort((g1, g2) -> {
            // 1. 直播中的排最前面
            boolean g1Live = "in_progress".equals(g1.getStatus_state());
            boolean g2Live = "in_progress".equals(g2.getStatus_state());

            if (g1Live && !g2Live) return -1;
            if (!g1Live && g2Live) return 1;

            // 2. 都是直播中 → 按规则排
            if (g1Live && g2Live) {
                // period 大的优先
                int p1 = g1.getPeriod() != null ? g1.getPeriod() : 0;
                int p2 = g2.getPeriod() != null ? g2.getPeriod() : 0;
                if (p1 != p2) return Integer.compare(p2, p1);

                // 分差小的优先
                int diff1 = Math.abs(g1.getHome_team_score() - g1.getVisitor_team_score());
                int diff2 = Math.abs(g2.getHome_team_score() - g2.getVisitor_team_score());
                if (diff1 != diff2) return Integer.compare(diff1, diff2);

                // 剩时少的优先
                return compareTime(g1.getTime(), g2.getTime());
            }

            // 3. 都不是直播中 → 保持原顺序
            return 0;
        });
        return games;
    }
    private int getStatusOrder(Game game){
        String state = game.getStatus_state();
        return switch (state) {
            case "in_progress" -> 0;
            case "scheduled" -> 1;
            case "final" -> 2;
            default -> 3;
        };
    }
    private int compareTime(String t1,String t2){
        if (t1==null || t2==null)return 0;
        int time1 = Integer.parseInt(t1.replace(":", ""));
        int time2 = Integer.parseInt(t2.replace(":", ""));
        return Integer.compare(time1,time2);
    }

}
