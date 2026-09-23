package com.jr.dto.teams;

import com.jr.dto.common.Team;
import lombok.Data;
import java.util.List;

@Data
public class TeamResponse {
    private List<Team> data;
}