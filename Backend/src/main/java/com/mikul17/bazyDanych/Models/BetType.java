package com.mikul17.bazyDanych.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "bet_type")
public class BetType {
    @Id
    @Column(name = "betTypeId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String betStat;
    private String betTypeCode;
    @Column(columnDefinition = "TINYINT(1)")
    private Boolean team;
    @Column (columnDefinition = "DECIMAL(2,1)")
    private Double targetValue;
}
