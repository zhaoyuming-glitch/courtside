package com.jr.dto.rating;

import lombok.Data;

@Data
public class RatingStatsDTO {
    private Integer userScore;
    private Double avgRating;
    private Integer ratingCount;
}
