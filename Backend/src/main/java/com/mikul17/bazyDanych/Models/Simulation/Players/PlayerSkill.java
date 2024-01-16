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
@Entity(name = "player_skill")
public class PlayerSkill {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;

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
    //goalkeeper stats
    private Integer closeRangeDefending;
    private Integer longRangeDefending;
    private Integer reflex;
    private Integer diving;
}
