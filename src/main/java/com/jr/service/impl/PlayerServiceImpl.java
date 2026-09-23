package com.jr.service.impl;

import com.jr.client.PlayerClient;
import com.jr.dto.players.Player;
import com.jr.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

import java.util.List;
@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerClient playerClient;
    @Override
    public Player getPlayer(Integer playerId) {
        return playerClient.getPlayerById(playerId);
    }

    @Override
    public List<Player> searchPlayers(String keyword) {
        return Collections.emptyList();
    }
}
