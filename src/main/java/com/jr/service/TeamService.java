package com.jr.service;

import com.jr.dto.common.Team;

import java.util.List;

public interface TeamService {
    List<Team> getAllTeams();
    Team getTeamById(Integer teamId);
    List<Team> getTeamsByConference(String conference);
}
