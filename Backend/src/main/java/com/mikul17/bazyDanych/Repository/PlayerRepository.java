package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Simulation.Players.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}