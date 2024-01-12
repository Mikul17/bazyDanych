package com.mikul17.bazyDanych.Response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchResponse {
    private Long id;
    private String homeTeam;
    private String awayTeam;
    private String league;
    private Timestamp matchDate;
}
