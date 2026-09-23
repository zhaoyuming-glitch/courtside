package com.jr.dto.games;

import com.jr.dto.common.Meta;
import lombok.Data;

import java.util.List;
@Data
public class GameResponse {
    private List<Game> data;
    private Meta meta;
}
