package com.mikul17.bazyDanych.Models.Matches;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @ColumnDefault("0")
    private Integer goalsScored;
        @ColumnDefault("0")
    private Integer possession;
        @ColumnDefault("0")
    private Integer shots;
            @ColumnDefault("0")
    private Integer shotsOnTarget;
            @ColumnDefault("0")
    private Integer passes;
            @ColumnDefault("0")
    private Integer corners;
            @ColumnDefault("0")
    private Integer throwIns;
            @ColumnDefault("0")
    private Integer freeKicks;
            @ColumnDefault("0")
    private Integer penalties;
            @ColumnDefault("0")
    private Integer yellowCards;
            @ColumnDefault("0")
    private Integer redCards;
            @ColumnDefault("0")
    private Integer fouls;

}
