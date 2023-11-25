package com.mikul17.bazyDanych.Models.Players;

import com.mikul17.bazyDanych.Models.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Boolean isBenched;
    private Boolean isRedCarded;
    private Boolean isInjured;
    private LocalDate injuredUntil;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
