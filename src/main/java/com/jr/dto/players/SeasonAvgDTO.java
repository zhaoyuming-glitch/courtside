package com.jr.dto.players;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonAvgDTO {
    private Double points;
    private Double rebounds;
    private Double assists;


    public String getPointsStr() {
        return points != null ? String.format("%.1f", points) : "--";
    }

    public String getReboundsStr() {
        return rebounds != null ? String.format("%.1f", rebounds) : "--";
    }

    public String getAssistsStr() {
        return assists != null ? String.format("%.1f", assists) : "--";
    }
}
