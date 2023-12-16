package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Matches.MatchStats;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchStatsRepository extends JpaRepository<MatchStats, Long> {
    Optional<MatchStats> findByMatchAndTeam(Match match, Long team);
}
