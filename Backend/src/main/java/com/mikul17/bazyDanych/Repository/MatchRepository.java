package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Simulation.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    public List<Match> findAllByMatchDateAfter(Timestamp now);

    List<Match> findAllByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(Long homeTeam_id, Long awayTeam_id);
    List<Match> findAllByMatchDateBeforeAndHomeTeamIdOrAwayTeamIdOrderByMatchDateAsc(Timestamp currentDate, Long homeTeamId, Long awayTeamId);
    List<Match> findAllByMatchDateAfterAndHomeTeamIdOrAwayTeamIdOrderByMatchDateAsc(Timestamp now, Long teamId, Long teamId1);

    List<Match> findAllByLeagueAndMatchDateAfter(League league, Timestamp matchDate);
    List<Match> findAllByLeagueAndMatchDateBetweenOrderByMatchDateAsc(League league, Timestamp start, Timestamp end);
}
