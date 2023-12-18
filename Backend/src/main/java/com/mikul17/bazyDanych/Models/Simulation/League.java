package com.mikul17.bazyDanych.Models.Simulation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mikul17.bazyDanych.Models.Matches.Match;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "league", cascade = CascadeType.REMOVE)
    private List<Team> team;

    @JsonIgnore
    @OneToMany(mappedBy = "league", cascade = CascadeType.REMOVE)
    private List<Match> matches;
}