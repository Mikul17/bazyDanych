package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Simulation.League;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Pageable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    public List<Match> findAllByMatchDateAfter(Timestamp now);
    public List<Match> findAllByMatchDateAfterOrderByMatchDateAsc(Timestamp matchDate);
    public List<Match> findAllByMatchDateBetweenOrderByMatchDate(Timestamp beginning, Timestamp end);
    List<Match> findAllByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(Long homeTeam_id, Long awayTeam_id);
    List<Match> findAllByMatchDateBeforeAndHomeTeamIdOrAwayTeamIdOrderByMatchDateAsc(Timestamp currentDate, Long homeTeamId, Long awayTeamId);
    List<Match> findAllByMatchDateAfterAndHomeTeamIdOrAwayTeamIdOrderByMatchDateAsc(Timestamp now, Long teamId, Long teamId1);

    List<Match> findAllByLeagueAndMatchDateAfter(League league, Timestamp matchDate);
    List<Match> findAllByLeagueAndMatchDateBetweenOrderByMatchDateAsc(League league, Timestamp start, Timestamp end);

    List<Match> findFirst5ByHomeTeamOrAwayTeamAndMatchDateBeforeOrderByMatchDateDesc(Team homeTeam, Team awayTeam, Timestamp matchDate);

    @Query("SELECT m FROM matches m WHERE (m.homeTeam = :team OR m.awayTeam = :team) AND m.matchDate < :date AND m.id <> :excludeMatchId ORDER BY m.matchDate DESC")
    List<Match> findHistoryExcludingCurrentMatch(@Param("team") Team team, @Param("date") Timestamp date, @Param("excludeMatchId") Long excludeMatchId, Pageable pageable);

}
