package com.mikul17.bazyDanych.Response;

import com.mikul17.bazyDanych.Models.Matches.Match;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchStatResponse {
    private String teamName;
    private Long matchId;
    private Integer goalsScored;
    private Integer possession;
    private Integer shots;
    private Integer shotsOnTarget;
    private Integer passes;
    private Integer corners;
    private Integer throwIns;
    private Integer freeKicks;
    private Integer penalties;
    private Integer yellowCards;
    private Integer redCards;
    private Integer fouls;
}
