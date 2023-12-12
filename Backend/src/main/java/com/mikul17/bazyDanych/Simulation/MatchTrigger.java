package com.mikul17.bazyDanych.Simulation;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchTrigger {

    private final MatchService matchService;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void checkForUpcomingMatches() {
        List<Match> upcomingMatches = matchService.getUpcomingMatches();
        Date now = new Date();
        for (Match match : upcomingMatches) {
            if (match.getMatchDate().equals(now)) {
                // Code to launch a new thread with simulateMatch()
            }
        }
    }
}
