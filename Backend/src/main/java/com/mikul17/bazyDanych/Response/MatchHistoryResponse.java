package com.mikul17.bazyDanych.Response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchHistoryResponse {
    private Long matchId;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeTeamGoals;
    private Integer awayTeamGoals;
    private LocalDate matchDate;
}
