package com.jr.dto.stats;

import com.jr.dto.common.Meta;
import lombok.Data;

import java.util.List;
@Data
public class StatsResponse {
    private List<PlayerStat> data; // 球员数据列表
    private Meta meta;             // 分页信息
}
