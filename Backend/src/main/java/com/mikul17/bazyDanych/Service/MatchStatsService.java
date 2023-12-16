package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Matches.MatchStats;
import com.mikul17.bazyDanych.Repository.MatchStatsRepository;
import com.mikul17.bazyDanych.Response.MatchStatResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchStatsService {
    private final Logger logger = LoggerFactory.getLogger(MatchStatsService.class);
    private final MatchStatsRepository repository;
    private final MatchService matchService;
    private final TeamService teamService;

    public MatchStatResponse getAllStatsForTeam(Long matchId, Boolean isHomeTeam){
        try{
            Match match = matchService.getMatchById(matchId);

            MatchStats stats = isHomeTeam ? repository.findByMatchAndTeam(match,match.getHomeTeam().getId())
                    .orElseThrow(()-> new ServiceException
                            ("Match with home team with id: "+match.getHomeTeam().getId()+" doesn't exist")):
                    repository.findByMatchAndTeam(match,match.getHomeTeam().getId()).orElseThrow(()-> new ServiceException
                            ("Match with home team with id: "+match.getAwayTeam().getId()+" doesn't exist"));
            return mapMatchStatToResponse(stats);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public MatchStatResponse saveStats(MatchStats stats){
        try{
            repository.save(stats);
            return mapMatchStatToResponse(stats);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    private MatchStatResponse mapMatchStatToResponse(MatchStats stats){
        return MatchStatResponse.builder()
                .teamName(teamService.getTeamNameById(stats.getTeam()))
                .matchId(stats.getMatch().getId())
                .goalsScored(stats.getGoalsScored())
                .possession(stats.getPossession())
                .shots(stats.getShots())
                .shotsOnTarget(stats.getShotsOnTarget())
                .passes(stats.getPasses())
                .corners(stats.getCorners())
                .throwIns(stats.getThrowIns())
                .freeKicks(stats.getFreeKicks())
                .penalties(stats.getPenalties())
                .yellowCards(stats.getYellowCards())
                .redCards(stats.getRedCards())
                .fouls(stats.getFouls())
                .build();
    }
}
