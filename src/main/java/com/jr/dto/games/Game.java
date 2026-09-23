package com.jr.dto.games;

import com.jr.dto.common.Team;
import lombok.Data;

@Data
public class Game {
    private Integer id;
    private String date;
    private Integer season;
    private String status;
    private String status_state;
    private Integer period;
    private String time;
    private Boolean postseason;
    private Integer home_team_score;
    private Integer visitor_team_score;
    private String datetime;
    private Integer home_q1;
    private Integer home_q2;
    private Integer home_q3;
    private Integer home_q4;
    private Integer home_ot1;
    private Integer home_ot2;
    private Integer visitor_q1;
    private Integer visitor_q2;
    private Integer visitor_q3;
    private Integer visitor_q4;
    private Integer visitor_ot1;
    private Integer visitor_ot2;
    private Team home_team;
    private Team visitor_team;
}