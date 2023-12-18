package com.mikul17.bazyDanych.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreResponse {
    private Integer homeTeamGoals;
    private Integer awayTeamGoals;
}