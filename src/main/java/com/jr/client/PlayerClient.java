package com.jr.client;

import com.jr.dto.players.Player;
import com.jr.dto.players.PlayerResponse;
import com.jr.dto.players.SinglePlayerResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class PlayerClient extends BaseNbaClient {

    /**
     * 通用查询球员列表
     * 支持所有 balldontlie /players 的参数：search、team_ids[] 等
     */
    // 列表接口 → 用 PlayerResponse
    public List<Player> getPlayers(Map<String, String> params) {
        PlayerResponse response = getWithAuth("/players", params, PlayerResponse.class);
        return response != null ? response.getData() : Collections.emptyList();
    }


    public Player getPlayerById(Integer playerId) {
        SinglePlayerResponse response = getWithAuth("/players/" + playerId, null, SinglePlayerResponse.class);
        return response != null ? response.getData() : null;
    }
}