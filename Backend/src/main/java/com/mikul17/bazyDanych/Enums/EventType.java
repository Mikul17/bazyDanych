package com.mikul17.bazyDanych.Enums;

import com.mikul17.bazyDanych.Models.Simulation.Players.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum EventType {
    YellowCard("yellowCard"),
    RedCard("redCard"),
    Warning("warning"),
    Injury("injury"),
    Penalty("penalty"),
    Freekick("freekick"),
    Goal("goal"),
    HalfTime("halfTime"),
    Beginning("beginning"),
    End("end");

    private final String desc;
}
