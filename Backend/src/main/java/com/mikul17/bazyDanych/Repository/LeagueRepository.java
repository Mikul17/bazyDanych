package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Simulation.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
}