package com.mikul17.bazyDanych.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BetTypeRequest {
    @NotBlank
    private String betStat;
    @NotBlank
    private String betTypeCode;
    private Integer team;
    private Double targetValue;
}
