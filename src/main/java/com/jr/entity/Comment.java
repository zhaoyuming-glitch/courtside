package com.jr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    private Integer comment_id;
    private Integer user_id;          // 评论人ID
    private Integer player_id;        // 球员ID（可为空）
    private Integer game_id;          // 比赛ID（可为空）
    private Integer post_id;          // 帖子ID（可为空）
    private String content;          // 评论内容
    private LocalDateTime create_time;
}