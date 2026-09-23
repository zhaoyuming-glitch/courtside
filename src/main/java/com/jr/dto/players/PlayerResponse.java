package com.jr.dto.players;

import com.jr.dto.common.Meta;
import lombok.Data;
import java.util.List;

@Data
public class PlayerResponse {
    private List<Player> data;
    private Meta meta;
}