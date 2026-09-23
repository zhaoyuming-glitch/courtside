package com.jr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Integer user_id;
    private String username;
    private String account;
    private String password;
    private String email;
    private String avatar;
    private LocalDateTime create_time;
}