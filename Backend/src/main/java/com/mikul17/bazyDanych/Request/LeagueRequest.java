package com.mikul17.bazyDanych.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeagueRequest {
    private String leagueName;
    private String country;
}
