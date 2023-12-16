package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Simulation.Players.Player;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByTeam (Team team);
}