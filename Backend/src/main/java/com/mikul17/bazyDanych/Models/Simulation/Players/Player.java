package com.mikul17.bazyDanych.Models.Simulation.Players;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    private String firstName;
    private String lastName;
    private String position;
    private Integer age;
    private String nationality;
    @Column(columnDefinition = "boolean default false")
    private Boolean isBenched;
    @Column(columnDefinition = "boolean default false")
    private Boolean isRedCarded;
    @Column(columnDefinition = "boolean default false")
    private Boolean isInjured;
    private LocalDate injuredUntil;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;
}
