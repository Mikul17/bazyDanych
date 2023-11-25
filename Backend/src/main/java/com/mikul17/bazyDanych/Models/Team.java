package com.mikul17.bazyDanych.Models;

import com.mikul17.bazyDanych.Models.Players.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    private String teamName;
    private Integer wins;
    private Integer draws;
    private Integer loses;
    private Integer goalsScored;
    private Integer goalsConceded;
    private Integer leaguePoints;

    @ManyToOne(targetEntity = League.class)
    @JoinColumn(name = "league_id")
    private League league;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    @OneToOne(targetEntity = Player.class)
    @JoinColumn(name = "captain_id")
    private Player captain;

}
