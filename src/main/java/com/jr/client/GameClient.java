package com.jr.client;

import com.jr.dto.games.Game;
import com.jr.dto.games.GameResponse;
import com.jr.dto.games.SingleGameResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class GameClient extends BaseNbaClient {

    /**
     * 通用查询比赛列表
     * 支持所有 balldontlie /games 的参数：dates[]、seasons[]、team_ids[]、start_date、end_date、postseason 等
     */
    public List<Game> getGames(Map<String, String> params) {
        try {
            GameResponse response = getWithAuth("/games", params, GameResponse.class);
            return response != null ? response.getData() : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("GameClient.getGames 请求失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 根据 ID 查单场比赛
     */
    public Game getGameById(Integer gameId) {
        try {
            SingleGameResponse response = getWithAuth("/games/" + gameId, null, SingleGameResponse.class);
            return response != null ? response.getData() : null;
        } catch (Exception e) {
            System.err.println("GameClient.getGameById 请求失败: " + e.getMessage());
            return null;
        }
    }
}