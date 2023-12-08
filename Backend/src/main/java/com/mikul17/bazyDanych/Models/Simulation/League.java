package com.mikul17.bazyDanych.Models.Simulation;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "league")
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "league_id")
    private Long id;
    private String leagueName;
    @Column(name = "origin_country")
    private String country;
    @Column(columnDefinition = "integer default 0")
    private Integer remainingMatches;
    @Column(columnDefinition = "integer default 0")
    private Integer season;
}