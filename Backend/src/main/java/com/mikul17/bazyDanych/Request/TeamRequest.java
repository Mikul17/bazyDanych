package com.mikul17.bazyDanych.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamRequest {
    private String teamName;
    private Long leagueId;
}
