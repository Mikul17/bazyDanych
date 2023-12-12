package com.mikul17.bazyDanych.Request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequest {
    private Timestamp date;
    private Long homeTeamId;
    private Long awayTeamId;
    private Long leagueId;
}
