package com.jr.controller;

import com.jr.dto.rating.RatingStatsDTO;
import com.jr.entity.Rating;
import com.jr.enums.RatingResult;
import com.jr.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/courtside/rating")
public class RatingController {
    @Autowired
    private RatingService service;
    @RequestMapping("/getPlayerRating")
    @ResponseBody
    public RatingStatsDTO getPlayerRating(Integer player_id, Integer game_id, Integer user_id){
        return service.getPlayerRating(player_id, game_id, user_id);
    }
    @RequestMapping("/submitRating")
    @ResponseBody
    public RatingResult submitRating(Rating rating){
        return service.submitRating(rating);
    }

}
