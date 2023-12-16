package com.mikul17.bazyDanych.Simulation;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Service.LeagueService;
import com.mikul17.bazyDanych.Service.MatchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchTrigger {

    private final MatchService matchService;
    private final LeagueService leagueService;
    private final Logger logger = LoggerFactory.getLogger(MatchTrigger.class);

    @Scheduled(cron = "0 0/1 * * * *") //method will run every quarter of hour
    public void checkForUpcomingMatches() {
        long twoMinutesInMillis = 2 * 60 * 1000;
        Date now = new Date();
        logger.info("Comparing now ["+now+"] with current matches");
        List<Match> upcomingMatches = matchService.getUpcomingMatches();

        Date twoMinutesBefore = new Date(now.getTime() - twoMinutesInMillis);
        Date twoMinutesAfter = new Date(now.getTime() + twoMinutesInMillis);

        for (Match match : upcomingMatches) {
            //check for minor time mismatch +/- 2 min from scheduled matchDate
            if (!match.getMatchDate().before(twoMinutesBefore) && !match.getMatchDate().after(twoMinutesAfter)) {
                logger.info("Starting match: "+match.getHomeTeam()+" vs "+ match.getAwayTeam());
                new Thread(() -> {
                    //TODO implement simulateMatch method
                   // matchService.simulateMatch(match);
                    leagueService.calculateRemainingMatches(match.getLeague(),false);
                }).start();
            }
        }
    }
}
