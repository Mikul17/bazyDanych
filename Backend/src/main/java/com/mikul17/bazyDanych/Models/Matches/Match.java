package com.mikul17.bazyDanych.Models.Matches;

import com.mikul17.bazyDanych.Models.Simulation.League;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Must be named in plural form because match is a reserved word in SQL
@Entity(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp matchDate;

    @ManyToOne(targetEntity = Team.class)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne(targetEntity = Team.class)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @ManyToOne(targetEntity = League.class)
    @JoinColumn(name = "league_id")
    private League league;


    public Match(Team homeTeam, Team awayTeam, Timestamp matchDate, League league){
        this.homeTeam=homeTeam;
        this.awayTeam=awayTeam;
        this.matchDate=matchDate;
        this.league=league;
    }
}
