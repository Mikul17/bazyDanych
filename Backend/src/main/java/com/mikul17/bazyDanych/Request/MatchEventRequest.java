package com.mikul17.bazyDanych.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchEventRequest {
    private Long matchId;
    private Long playerId;
    private Integer minute;
    private String actionType;
    private String actionDescription;
}
