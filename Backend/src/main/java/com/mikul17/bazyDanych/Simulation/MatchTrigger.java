package com.mikul17.bazyDanych.Simulation;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Repository.*;
import com.mikul17.bazyDanych.Service.BetService;
import com.mikul17.bazyDanych.Service.CouponService;
import com.mikul17.bazyDanych.Service.LeagueService;
import com.mikul17.bazyDanych.Service.MatchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchTrigger {

    private final MatchService matchService;
    private final LeagueService leagueService;
    private final BetService betService;
    private final CouponService couponService;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerSkillRepository playerSkillRepository;
    private final PlayerStatRepository playerStatRepository;
    private final MatchStatsRepository matchStatsRepository;
    private final MatchEventRepository matchEventRepository;
    private final Logger logger = LoggerFactory.getLogger(MatchTrigger.class);

//    @Scheduled(cron = "0 0/15 * * * *")
    @Scheduled(fixedRate = 1000*60)
    public void checkForUpcomingMatches() {
        long twoMinutesInMillis = 2 * 60 * 1000;
        long thirtySecInMillis = 30*1000;
        Date now = new Date();
        logger.info("Comparing now ["+now+"] with current matches");
        List<Match> upcomingMatches = matchService.getTodayMatches();

        Date twoMinutesBefore = new Date(now.getTime() - thirtySecInMillis);
        Date twoMinutesAfter = new Date(now.getTime() + thirtySecInMillis);

        for (Match match : upcomingMatches) {
            if (!match.getMatchDate().before(twoMinutesBefore) && !match.getMatchDate().after(twoMinutesAfter)) {
                logger.info("Starting match: "+match.getHomeTeam()+" vs "+ match.getAwayTeam());
                new Thread(() -> {
                    Simulation simulation = new Simulation(match,
                            playerRepository,teamRepository,playerSkillRepository,playerStatRepository,
                            matchStatsRepository,matchEventRepository);
                    simulation.simulateMatch();
                    leagueService.calculateRemainingMatches(match.getLeague(),false);
                    betService.updateBetsStatusAfterMatch(match);
                    couponService.updateCouponsAfterMatch(match.getId());
                }).start();
            }
        }
    }
}
