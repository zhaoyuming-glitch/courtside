package com.jr.client;

import com.jr.dto.stats.PlayerStat;
import com.jr.dto.stats.StatsResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient extends BaseNbaClient {

    /**
     * 通用查询数据统计
     * 支持所有 balldontlie /stats 的参数：game_ids[]、player_ids[]、dates[]、seasons[] 等
     */
    public List<PlayerStat> getStats(Map<String, String> params) {
        try {
            StatsResponse response = getWithAuth("/stats", params, StatsResponse.class);
            return response != null ? response.getData() : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("StatsClient.getStats 请求失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // 可选：如果你经常需要按单场比赛查，保留这个作为便捷方法（内部调 getStats）
    public List<PlayerStat> getStatsByGame(Integer gameId) {
        Map<String, String> params = new java.util.HashMap<>();
        params.put("game_ids[]", String.valueOf(gameId));
        return getStats(params);
    }
}