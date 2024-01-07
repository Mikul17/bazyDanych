package com.mikul17.bazyDanych.Response;

import com.mikul17.bazyDanych.Models.Coupons.BetType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BetResponse{
    private Long id;
    private Long matchId;
    private String homeTeam;
    private String awayTeam;
    private Double odds;
    private BetType betType;
    private Integer betStatus;
}
