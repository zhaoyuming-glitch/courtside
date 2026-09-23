package com.jr.service.impl;

import com.jr.client.AiClient;
import com.jr.dto.games.Game;
import com.jr.dto.stats.PlayerStat;
import com.jr.service.GameService;
import com.jr.service.RecapService;
import com.jr.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RecapServiceImpl implements RecapService {
    @Autowired
    private AiClient client;
    @Autowired
    private GameService gameService;
    @Autowired
    private StatsService statsService;
    @Autowired
    private StringRedisTemplate redis;
    private static final String KEY_PREFIX="recap:";
    @Override
    public String getRecap(int gameId) {
        String key =KEY_PREFIX+ gameId;
        String cached = redis.opsForValue().get(key);
        if (cached!=null)return cached;
        String content = generateRecap(gameId);
        redis.opsForValue().set(key,content,24, TimeUnit.HOURS);
        return content;
    }

    @Override
    public String regenerate(int gameId) {
        redis.delete(KEY_PREFIX+gameId);
         return getRecap(gameId);

    }
    private String generateRecap(int gameId) {
        Game game = gameService.getGameById(gameId);
        List<PlayerStat> homeStats = statsService.getHomeTeamStatsByScore(gameId);
        List<PlayerStat> visitorStats = statsService.getVisitorTeamStatsByScore(gameId);
        boolean gameEmpty = (game == null);
        boolean homeEmpty = (homeStats == null || homeStats.isEmpty());
        boolean visitorEmpty = (visitorStats == null || visitorStats.isEmpty());
        if (gameEmpty && homeEmpty && visitorEmpty) {
            return "无法获取比赛信息";
        }
        String systemPrompt = "你是一名资深NBA篮球记者，擅长用简洁、生动的语言撰写比赛总结。"
                + "你的文字风格专业、有观点、有数据支撑，不空洞。";

        String userPrompt = "请根据以下比赛数据，写一段赛后总结。\n\n"
                + "【比赛信息】\n" + game + "\n\n"
                + "【主队球员数据】\n" + homeStats + "\n\n"
                + "【客队球员数据】\n" + visitorStats + "\n\n"
                + "要求：\n"
                + "1. 字数 200 字左右\n"
                + "2. 分 2~3 段，用空行分隔\n"
                + "3. 中文，语言简洁专业\n"
                + "4. 突出关键球员表现和胜负原因\n"
                + "5. 不要罗列数据，要有分析和观点";

        return client.generate(systemPrompt, userPrompt);
    }
}
