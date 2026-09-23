package com.jr.service;

import com.jr.dto.rating.RatingStatsDTO;
import com.jr.entity.Rating;
import com.jr.enums.RatingResult;

public interface RatingService {
   RatingResult submitRating(Rating rating);
   RatingStatsDTO getPlayerRating(Integer player_id, Integer game_id, Integer user_id);

}
