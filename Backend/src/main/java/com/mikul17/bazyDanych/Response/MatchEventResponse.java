package com.mikul17.bazyDanych.Response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchEventResponse {
    private Long id;
    private boolean isHomeTeam;
    private String actionType;
    private String desc;
}
