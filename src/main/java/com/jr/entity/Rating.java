package com.jr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rating {
   private Integer rating_id;
   private Integer user_id;
   private Integer player_id;
   private Integer game_id;
   private Integer score;
   private LocalDateTime create_time;
}
