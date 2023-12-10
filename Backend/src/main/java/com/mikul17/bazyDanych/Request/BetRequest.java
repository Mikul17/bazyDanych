package com.mikul17.bazyDanych.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BetRequest {
    private Long matchId;
    private Double odds;
    private Long betTypeId;
}
