package com.mikul17.bazyDanych.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum BetStat {
    GOALS("goals"),
    SHOTS("shots"),
    SHOTS_ON_TARGET("shotsOnTarget"),
    PASSES("passes"),
    CORNERS("corners"),
    YELLOW_CARDS("yellowCards"),
    FOULS("fouls"),
    POSSESSION("possession"),
    PENALTIES("penalties"),
    RED_CARDS("redCards"),
    SCORE("score");

    private final String stat;
}
