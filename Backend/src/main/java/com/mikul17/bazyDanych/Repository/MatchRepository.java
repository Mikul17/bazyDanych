package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Matches.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    public List<Match> findAllByMatchDateAfter(Timestamp now);

    List<Match> findAllByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(Long homeTeam_id, Long awayTeam_id);
    List<Match> findAllByMatchDateBeforeAndHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(Timestamp currentDate, Long homeTeamId, Long awayTeamId);
    List<Match> findAllByMatchDateAfterAndHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(Timestamp now, Long teamId, Long teamId1);
}
