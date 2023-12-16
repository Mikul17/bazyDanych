package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Simulation.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>{
    List<Team> findAllByLeagueId(Long id);
    Optional<Team> findByTeamName(String teamName);
    @Query("SELECT t FROM team t WHERE t.league.id = :leagueId ORDER BY t.leaguePoints DESC, (t.goalsScored - t.goalsConceded) DESC")
    List<Team> findAllByLeagueIdOrderByPointsAndGoalDifference(Long leagueId);
}
