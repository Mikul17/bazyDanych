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
    private List<Bet> bets;
    private Double stake;
}
