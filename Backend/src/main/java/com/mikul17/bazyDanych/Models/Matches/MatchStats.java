package com.mikul17.bazyDanych.Models.Matches;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//Must be named in plural form because match is a reserved word in SQL
@Entity(name = "matches_stats")
public class MatchStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_stats_id")
    private Long id;
    @Column(name = "team_id")
    private Long team;
    @ManyToOne(targetEntity = Match.class)
    @JoinColumn(name = "match_id")
    private Match match;
    private Integer goalsScored;
    private Integer possession;
    private Integer shots;
    private Integer shotsOnTarget;
    private Integer passes;
    private Integer corners;
    private Integer throwIns;
    private Integer freeKicks;
    private Integer penalties;
    private Integer yellowCards;
    private Integer redCards;
    private Integer fouls;
}
