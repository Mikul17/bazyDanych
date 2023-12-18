package com.mikul17.bazyDanych.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum BetTypeCode {
    DIRECT("direct"),
    OVER("over"),
    UNDER("under");

    private final String code;
}
