package com.mikul17.bazyDanych.Models.Matches;

import com.mikul17.bazyDanych.Models.Simulation.Players.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "matches_events")
public class MatchEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_event_id")
    private Long Id;

    @ManyToOne(targetEntity = Match.class)
    @JoinColumn(name = "match_id")
    private Match match;

    private Integer minute;
    @Column(columnDefinition = "VARCHAR(20)")
    private String actionType;
    @Column(columnDefinition = "TEXT")
    private String actionDescription;

    @ManyToOne(targetEntity = Player.class)
    @JoinColumn(name = "player_id")
    private Player player;
}
