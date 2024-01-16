package com.mikul17.bazyDanych.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  Stat {
    GOALS("goals"),
    POSSESSION("possession"),
    SHOTS("shots"),
    SHOTS_ON_TARGET("shotsOnTarget"),
    PASSES("passes"),
    CORNERS("corners"),
    THROW_INS("throwIns"),
    FREE_KICKS("freeKicks"),
    PENALTIES("penalties"),
    YELLOW_CARDS("yellowCards"),
    RED_CARDS("redCards"),
    FOULS("fouls");

    private final String stat;
}
