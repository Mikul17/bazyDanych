package com.mikul17.bazyDanych.Models.Simulation.Players;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "player_stat")
public class PlayerStat {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;

    private Integer goalsScored;
    private Integer assists;
    private Integer yellowCards;
    private Integer redCards;
    private Integer fouls;
    private Integer gamesPlayed;
}
