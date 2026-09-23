package com.jr.service.impl;

import com.jr.dto.rating.RatingStatsDTO;
import com.jr.entity.Rating;
import com.jr.enums.RatingResult;
import com.jr.mapper.RatingMapper;
import com.jr.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingMapper mapper;
    @Override
    public RatingResult submitRating(Rating rating) {
        Rating rating1 = mapper.selectGamePlayerRating(rating.getGame_id(), rating.getPlayer_id(), rating.getUser_id());
        if (rating1==null){
            return mapper.addGamePlayerRating(rating)>0? RatingResult.FIRST_SUCCESS:RatingResult.FIRST_FAIL;
        }
        else {
            rating1.setScore(rating.getScore());
            return mapper.updateGamePlayerRating(rating1) > 0?RatingResult.UPDATE_SUCCESS:RatingResult.UPDATE_FAIL;
        }

    }

    @Override
    public RatingStatsDTO getPlayerRating(Integer player_id, Integer game_id, Integer user_id) {
        Rating rating = mapper.selectGamePlayerRating(game_id, player_id, user_id);
        Double gamePlayerAverageRating = mapper.getGamePlayerAverageRating(game_id, player_id);
        Integer count = mapper.countGamePlayerRating(game_id, player_id);
        RatingStatsDTO ratingStatsDTO = new RatingStatsDTO();
        if (rating!=null)ratingStatsDTO.setUserScore(rating.getScore());
        if (gamePlayerAverageRating!=null)ratingStatsDTO.setAvgRating(Math.round(gamePlayerAverageRating*10)/10.0);
        ratingStatsDTO.setRatingCount(count);
        return ratingStatsDTO;


    }
}
