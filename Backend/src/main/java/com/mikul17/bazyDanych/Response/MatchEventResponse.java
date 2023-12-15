package com.mikul17.bazyDanych.Response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchEventResponse {
    private Long matchId;
    private Integer minute;
    private String actionType;
    private String actionDescription;
    private Long playerId;
}
