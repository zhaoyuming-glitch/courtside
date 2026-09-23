package com.jr.dto.common;

import lombok.Data;

@Data
public class  Team {
    private Integer id;
    private String conference;    // "East" / "West"
    private String division;      // "Central" / "Pacific" ...
    private String city;          // "Cleveland"
    private String name;          // "Cavaliers"
    private String full_name;      // "Cleveland Cavaliers"
    private String abbreviation;  // "CLE"
}