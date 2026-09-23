package com.jr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {
    private Integer post_id;
    private Integer user_id;
    private String title;
    private String content_url;
    private Integer view_count;
    private Integer like_count;
    private Integer comment_count;
    private String author;
    private LocalDateTime create_time;
}