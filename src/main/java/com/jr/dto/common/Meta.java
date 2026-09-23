package com.jr.dto.common;

import lombok.Data;

@Data
public  class Meta {
    private Integer next_cursor;   // balldontlie 用 next_cursor 做分页
    private Integer per_page;
}
