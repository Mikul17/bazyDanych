package com.mikul17.bazyDanych.Simulation;

import com.mikul17.bazyDanych.Models.Simulation.League;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Service.LeagueService;
import com.mikul17.bazyDanych.Service.MatchService;
import com.mikul17.bazyDanych.Service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MatchScheduler {

    private final MatchService matchService;
    private final TeamService teamService;
    private final LeagueService leagueService;
    private Random random = new Random();

    public void generateFixturesForLeagues() throws Exception {
        List<League> leagues = leagueService.getAllLeagues();
        for (League league : leagues) {
            List<Team> teams = teamService.getTeamsByLeagueId(league.getId());
            generateFixturesForLeague(teams, league);
        }
    }

    private void generateFixturesForLeague(List<Team> teams, League league) {
        for (Team homeTeam : teams) {
            for (Team awayTeam : teams) {
                if (!homeTeam.getId().equals(awayTeam.getId())) {
                    Timestamp matchDate = getRandomMatchDate();
                    Match homeMatch = new Match(homeTeam, awayTeam, matchDate, league);
                    matchService.saveMatch(homeMatch);
                }
            }
        }
    }

    private Long getRandomTimeOfDayInMillis() {
        long baseTimeOfDay = TimeUnit.HOURS.toMillis(8);
        long randomHoursToAdd = TimeUnit.HOURS.toMillis(random.nextInt(14));
        long randomMinutesToAdd = TimeUnit.MINUTES.toMillis(random.nextInt(4) * 15);
        return baseTimeOfDay + randomHoursToAdd + randomMinutesToAdd;
    }

    private Timestamp getRandomMatchDate() {
        long currentTime = System.currentTimeMillis();
        long randomDayInFuture = TimeUnit.DAYS.toMillis(random.nextInt(14));
        long oneDayInMillis = TimeUnit.DAYS.toMillis(1);
        long randomTimeInFuture = currentTime + oneDayInMillis +randomDayInFuture+ getRandomTimeOfDayInMillis();
        return new Timestamp(randomTimeInFuture);
    }
}
