package com.mikul17.bazyDanych.Models.Coupons;

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
    @Column(name = "bet_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String betStat;
    private String betTypeCode;
    private Boolean team;
    @Column (columnDefinition = "DECIMAL(2,1)")
    private Double targetValue;
}
