    package com.mikul17.bazyDanych.Simulation;

    import com.mikul17.bazyDanych.Models.Simulation.League;
    import com.mikul17.bazyDanych.Models.Simulation.Team;
    import com.mikul17.bazyDanych.Models.Matches.Match;
    import com.mikul17.bazyDanych.Service.LeagueService;
    import com.mikul17.bazyDanych.Service.MatchService;
    import com.mikul17.bazyDanych.Service.TeamService;
    import lombok.RequiredArgsConstructor;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Component;

    import java.sql.Timestamp;
    import java.time.Instant;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.util.*;
    import java.util.concurrent.TimeUnit;

    @Component
    @RequiredArgsConstructor
    public class MatchScheduler {

        private final MatchService matchService;
        private final TeamService teamService;
        private final LeagueService leagueService;
        private final Logger logger = LoggerFactory.getLogger(MatchScheduler.class);
        private final Map<Long, Set<Long>> teamSchedule = new HashMap<>();

        @Scheduled(fixedRate = 1000*60*60*24)// check every day
        public void scheduleMatchesForSeason() {
            try {
                List<League> leagues = leagueService.getAllLeagues();
                if(leagues.isEmpty()){
                    logger.error("Error: league table is empty");
                }
                for (League league : leagues) {
                    if (league.getRemainingMatches() == 0) {
                        List<Team> teams = teamService.getTeamsByLeagueId(league.getId());
                        logger.info("Scheduling league matches for: "+league.getLeagueName()+" | season: "+league.getSeason()+1);
                        generateFixturesForLeague(teams, league);
                        leagueService.incrementSeasons(league);
                        teamService.resetTeamStatsForWholeLeague(league);
                    }else {
                        logger.info(league.getLeagueName()+" has "+league.getRemainingMatches()+" matches left");
                    }
                }
                teamSchedule.clear();
            }catch (Exception e){
                logger.error("Error while scheduling matches: "+e.getMessage());
            }
        }
        private void generateFixturesForLeague(List<Team> teams, League league) {
            for (Team homeTeam : teams) {
                teamSchedule.putIfAbsent(homeTeam.getId(), new HashSet<>());
                for (Team awayTeam : teams) {
                    if (!homeTeam.getId().equals(awayTeam.getId())) {
                        Timestamp matchDate;
                        do {
                            matchDate = getRandomMatchDate();
                        } while (teamSchedule.get(homeTeam.getId()).contains(matchDate.getTime()));
                        teamSchedule.get(homeTeam.getId()).add(matchDate.getTime());
                        Match homeMatch = new Match(homeTeam, awayTeam, matchDate, league);
                        matchService.saveMatch(homeMatch);
                        leagueService.calculateRemainingMatches(league,true);
                    }
                }
            }
        }
        private Long getRandomTimeOfDayInMillis() {
            Random random = new Random();
            long baseTimeOfDay = TimeUnit.HOURS.toMillis(8);
            long randomHoursToAdd = TimeUnit.HOURS.toMillis(random.nextInt(13)); // Match can start no later than  21:45

            List<Integer> minuteOffsets = new ArrayList<>(Arrays.asList(0, 15, 30, 45));
            long randomMinutesToAdd = TimeUnit.MINUTES.toMillis(minuteOffsets.get(random.nextInt(minuteOffsets.size())));

            return baseTimeOfDay + randomHoursToAdd + randomMinutesToAdd;
        }
        private Timestamp getRandomMatchDate() {
            Random random = new Random();
            LocalDateTime midnight = LocalDate.now().atStartOfDay();
            long randomDayInFuture = TimeUnit.DAYS.toMillis(random.nextInt(13));
            long oneDayInMillis = TimeUnit.DAYS.toMillis(1);
            long randomTimeOfDayInMillis = getRandomTimeOfDayInMillis();

            long randomTimeInFuture = midnight.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    + oneDayInMillis + randomDayInFuture + randomTimeOfDayInMillis;

            LocalDateTime randomDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(randomTimeInFuture), ZoneId.systemDefault());

            return Timestamp.valueOf(randomDateTime);
        }


    }
