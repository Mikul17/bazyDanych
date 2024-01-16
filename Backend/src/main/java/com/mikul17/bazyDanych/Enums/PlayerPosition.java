package com.mikul17.bazyDanych.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum PlayerPosition {
    FORWARD("forward"),
    MIDFIELDER("midfielder"),
    DEFENDER("defender"),
    GOALKEEPER("goalkeeper");
    private final String position;
}
