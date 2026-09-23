package com.jr.dto.stats;




import com.jr.dto.common.Team;
import lombok.Data;

@Data
public class PlayerStat {
    private Integer id;                 // 记录ID
    private String min;                 // 上场时间（分钟）
    private Integer fgm;                // 投篮命中数
    private Integer fga;                // 投篮出手数
    private Double fg_pct;               // 投篮命中率
    private Integer fg3m;               // 三分命中数
    private Integer fg3a;               // 三分出手数
    private Double fg3_pct;              // 三分命中率
    private Integer ftm;                // 罚球命中数
    private Integer fta;                // 罚球出手数
    private Double ft_pct;               // 罚球命中率
    private Integer oreb;               // 进攻篮板
    private Integer dreb;               // 防守篮板
    private Integer reb;                // 总篮板
    private Integer ast;                // 助攻
    private Integer stl;                // 抢断
    private Integer blk;                // 盖帽
    private Integer turnover;           // 失误
    private Integer pf;                 // 个人犯规
    private Integer pts;                // 得分
    private Integer plus_minus;          // 正负值
    private Player player;              // 球员信息
    private Team team;                  // 球队信息
    private Game game;                  // 比赛信息
}
