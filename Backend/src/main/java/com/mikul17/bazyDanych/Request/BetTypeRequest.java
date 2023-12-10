package com.mikul17.bazyDanych.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BetTypeRequest {
    private String betStat;
    private String betTypeCode;
    private Boolean team;
    private Double targetValue;
}
