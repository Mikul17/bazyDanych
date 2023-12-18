package com.mikul17.bazyDanych.Request;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponRequest {
    //private List<Bet> bets;
    private List<Long> bets;
    private Double stake;
    private Long userId;
}
