package com.jr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User_role {
    private Integer role_id;
    private Integer user_id;
    private String role_code;
    private LocalDateTime create_time;
}
