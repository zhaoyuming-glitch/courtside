package com.jr.service;

import com.jr.dto.players.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {
    Player getPlayer(Integer playerId);
    List<Player> searchPlayers(String keyword);

}