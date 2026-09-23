package com.jr.client;

import com.jr.dto.common.Team;
import com.jr.dto.teams.TeamResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class TeamClient extends BaseNbaClient {

    /**
     * 通用查询球队列表
     * 支持所有 balldontlie /teams 的参数：conference、division 等
     */
    // 列表接口 → 用 TeamResponse
    public List<Team> getTeams(Map<String, String> params) {
        TeamResponse response = getWithAuth("/teams", params, TeamResponse.class);
        return response != null ? response.getData() : Collections.emptyList();
    }

    // 单条接口 → 复用 TeamResponse（因为返回的 data 是数组）
    public Team getTeamById(Integer teamId) {
        TeamResponse response = getWithAuth("/teams/" + teamId, null, TeamResponse.class);
        List<Team> data = response != null ? response.getData() : null;
        return data != null && !data.isEmpty() ? data.get(0) : null;
    }
}