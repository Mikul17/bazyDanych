package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Simulation.League;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import com.mikul17.bazyDanych.Repository.MatchRepository;
import com.mikul17.bazyDanych.Repository.TeamRepository;
import com.mikul17.bazyDanych.Request.MatchRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final LeagueService leagueService;

    public Match addMatch(MatchRequest request){
        try{
            Team homeTeam = teamRepository.findById(request.getHomeTeamId())
                    .orElseThrow(() -> new ServiceException("Error: couldn't find team with given id: "+request.getHomeTeamId()));

            Team awayTeam = teamRepository.findById(request.getAwayTeamId())
                    .orElseThrow(() -> new ServiceException("Error: couldn't find team with given id: "+request.getAwayTeamId()));

            League league = leagueService.getLeagueById(request.getLeagueId());

            Match match = Match.builder()
                    .matchDate(request.getDate())
                    .homeTeam(homeTeam)
                    .awayTeam(awayTeam)
                    .league(league)
                    .build();
            return matchRepository.save(match);
        }catch (Exception e){
            throw new ServiceException("Error while adding match: "+e.getMessage());
        }
    }
    public void saveMatch(Match match) {
        try{
            matchRepository.save(match);
        }catch (Exception e){
            throw new ServiceException("Error while saving match: "+e.getMessage());
        }
    }
    public List<Match> getUpcomingMatches(){
        try{
            Timestamp now = new Timestamp(System.currentTimeMillis());
            return matchRepository.findAllByMatchDateAfter(now);
        }catch (Exception e){
            throw new ServiceException("Error while getting upcoming matches: "+e.getMessage());
        }
    }
    public List<Match> getMatchesByTeam(Long teamId, boolean upcoming){
        try{
            teamRepository.findById(teamId).orElseThrow(
                    ()-> new ServiceException("Couldn't find team with given id: "+teamId));
            Timestamp now = new Timestamp(System.currentTimeMillis());
           return upcoming ? matchRepository.findAllByMatchDateAfterAndHomeTeamIdOrAwayTeamIdOrderByMatchDateAsc(now,teamId,teamId) :
                   matchRepository.findAllByMatchDateBeforeAndHomeTeamIdOrAwayTeamIdOrderByMatchDateAsc(now,teamId,teamId);
        }catch (Exception e){
            throw new ServiceException("Error: "+e.getMessage());
        }
    }
    public Match getMatchById(Long matchId) {
        try{
            return matchRepository.findById(matchId).orElseThrow(
                    ()->new ServiceException("Match not found"));
        }catch (Exception e){
            throw new ServiceException("Error: "+e.getMessage());
        }
    }
    public List<Match> getTodayMatchesByLeague (Long leagueId){
        try{
            League league = leagueService.getLeagueById(leagueId);
            LocalDate today = LocalDate.now();
            Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());
            Timestamp endOfDay = Timestamp.valueOf(LocalDateTime.of(today, LocalTime.MAX));
            return matchRepository.findAllByLeagueAndMatchDateBetweenOrderByMatchDateAsc(league,startOfDay,endOfDay);
        }catch (Exception e){
            throw new ServiceException("Error while fetching matches: "+e.getMessage());
        }
    }
    public List<Match> getUpcomingMatchesByLeague(Long leagueId){
        try{
            Timestamp now = new Timestamp(System.currentTimeMillis());
            League league = leagueService.getLeagueById(leagueId);
            return matchRepository.findAllByLeagueAndMatchDateAfter(league,now);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public List<Match> getAllMatches(){
        return matchRepository.findAll();
    }

 }