package com.mikul17.bazyDanych.Models.Simulation.Players;

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
@Entity(name = "player_skill")
public class PlayerSkill {

    @Id
    @OneToOne(targetEntity = Player.class)
    @JoinColumn(name = "player_id")
    private Long id;

    private Integer pace;
    private Integer dribbling;
    private Integer header;
    private Integer defending;
    private Integer closeRangeShooting;
    private Integer longRangeShooting;
    private Integer penalties;
    private Integer freeKick;
    private Integer shortPass;
    private Integer longPass;
    private Integer crossing;
    private Integer vision;
    private Integer closeRangeDefending;
    private Integer longRangeDefending;
    private Integer reflex;
    private Integer diving;
}
