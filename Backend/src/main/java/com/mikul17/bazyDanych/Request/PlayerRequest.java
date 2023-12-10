package com.mikul17.bazyDanych.Request;

import com.mikul17.bazyDanych.Models.Simulation.Players.PlayerSkill;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerRequest {
    private String firstName;
    private String lastName;
    private String position;
    private Integer age;
    private String nationality;
    private PlayerSkill playerSkill;
}
