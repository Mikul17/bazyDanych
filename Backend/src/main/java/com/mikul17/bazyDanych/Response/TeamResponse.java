package com.mikul17.bazyDanych.Response;

import lombok.*;

    @Getter
    @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamResponse {
    private Long id;
    private String teamName;
    private Integer wins;
    private Integer draws;
    private Integer loses;
    private Integer goalsScored;
    private Integer goalsConceded;
    private Integer leaguePoints;
}
