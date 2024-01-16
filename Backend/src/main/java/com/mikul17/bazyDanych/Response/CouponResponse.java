package com.mikul17.bazyDanych.Response;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponse {
    private Long id;
    private String creationDate;
    private Double totalOdds;
    private String couponStatus;
    private Double possibleWin;
    private Double stake;
    private Long userId;
    private List<BetResponse> bets;
}
