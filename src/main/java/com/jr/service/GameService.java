package com.jr.service;

import com.jr.dto.games.Game;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface GameService {
    List<Game>getScoreBoardGame();
    Game getGameById(int gameId);
    List<Game> getGamesByDate(String date);
    List<Game> getGamesByDateRange(String startDate, String endDate);
}
