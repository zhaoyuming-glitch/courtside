package com.jr.dto.stats;

import lombok.Data;

@Data
public class Game {
    private Integer id;              // 比赛ID
    private String date;             // 日期
    private Integer season;          // 赛季
    private String status;           // 状态
    private String status_state;     // 状态代码
    private Integer period;          // 节次
    private String time;             // 剩余时间
    private Boolean postseason;      // 是否季后赛
    private Boolean postponed;       // 是否延期
    private Integer home_team_score; // 主队得分
    private Integer visitor_team_score; // 客队得分
    private Integer home_team_id;    // 主队ID
    private Integer visitor_team_id; // 客队ID
    private String ist_stage;        // 季中锦标赛阶段
}