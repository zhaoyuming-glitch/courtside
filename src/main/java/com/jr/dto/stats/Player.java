package com.jr.dto.stats;




import lombok.Data;

@Data
public class Player {
    private Integer id;                 // 球员ID
    private String first_name;           // 名
    private String last_name;            // 姓
    private String position;            // 位置（G/F/C/G-F）
    private String height;              // 身高（6-6）
    private String weight;              // 体重（223）
    private String jersey_number;        // 球衣号码
    private String college;             // 毕业大学
    private String country;             // 国籍
    private Integer draft_year;          // 选秀年份
    private Integer draft_round;         // 选秀轮次
    private Integer draft_number;        // 选秀顺位
    private Integer team_id;             // 球队ID
}
