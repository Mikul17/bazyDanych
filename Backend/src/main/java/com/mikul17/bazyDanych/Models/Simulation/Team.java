package com.mikul17.bazyDanych.Models.Simulation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikul17.bazyDanych.Models.Simulation.Players.Player;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchProfile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    private String teamName;
    @Column(columnDefinition = "integer default 0")
    private Integer wins;
    @Column(columnDefinition = "integer default 0")
    private Integer draws;
    @Column(columnDefinition = "integer default 0")
    private Integer loses;
    @Column(columnDefinition = "integer default 0")
    private Integer goalsScored;
    @Column(columnDefinition = "integer default 0")
    private Integer goalsConceded;
    @Column(columnDefinition = "integer default 0")
    private Integer leaguePoints;

    @ManyToOne(targetEntity = League.class)
    @JoinColumn(name = "league_id")
    private League league;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Player> players = new ArrayList<>();

    @OneToOne(targetEntity = Player.class)
    @JoinColumn(name = "captain_id")
    private Player captain;
}
