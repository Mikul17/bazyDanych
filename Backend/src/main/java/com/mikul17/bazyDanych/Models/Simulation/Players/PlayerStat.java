package com.mikul17.bazyDanych.Models.Simulation.Players;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "player_stat")
public class PlayerStat {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(columnDefinition = "integer default 0")
    private Integer goalsScored;
    @Column(columnDefinition = "integer default 0")
    private Integer assists;
    @Column(columnDefinition = "integer default 0")
    private Integer yellowCards;
    @Column(columnDefinition = "integer default 0")
    private Integer redCards;
    @Column(columnDefinition = "integer default 0")
    private Integer fouls;
    @Column(columnDefinition = "integer default 0")
    private Integer gamesPlayed;
}
