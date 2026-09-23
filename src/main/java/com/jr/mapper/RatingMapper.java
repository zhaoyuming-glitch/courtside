package com.jr.mapper;

import com.jr.entity.Rating;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RatingMapper {

    @Insert("INSERT INTO rating(user_id, player_id, game_id, score, create_time) " +
            "VALUES(#{user_id}, #{player_id}, #{game_id}, #{score}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "rating_id")
    int addGamePlayerRating(Rating rating);

    @Update("UPDATE rating SET score = #{score} WHERE rating_id = #{rating_id}")
    int updateGamePlayerRating(Rating rating);

    @Select("SELECT * FROM rating " +
            "WHERE game_id = #{gameId} AND player_id = #{playerId} AND user_id = #{userId} " +
            "LIMIT 1")
    Rating selectGamePlayerRating(@Param("gameId") int gameId,
                                  @Param("playerId") int playerId,
                                  @Param("userId") int userId);

    @Select("SELECT COUNT(*) FROM rating " +
            "WHERE game_id = #{gameId} AND player_id = #{playerId}")
    Integer countGamePlayerRating(@Param("gameId") int gameId,
                              @Param("playerId") int playerId);

    @Select("SELECT IFNULL(AVG(score), 0) FROM rating " +
            "WHERE game_id = #{gameId} AND player_id = #{playerId}")
    Double getGamePlayerAverageRating(@Param("gameId") int gameId,
                                      @Param("playerId") int playerId);
}