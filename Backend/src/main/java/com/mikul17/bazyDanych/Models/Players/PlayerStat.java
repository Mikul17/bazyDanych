package com.mikul17.bazyDanych.Models.Players;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    @OneToOne(targetEntity = Player.class)
    @JoinColumn(name = "player_id")
    private Long id;
    private Integer goalsScored;
    private Integer assists;
    private Integer yellowCards;
    private Integer redCards;
    private Integer fouls;
    private Integer gamesPlayed;
}
