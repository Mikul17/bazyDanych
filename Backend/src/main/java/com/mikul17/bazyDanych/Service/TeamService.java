package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Simulation.League;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import com.mikul17.bazyDanych.Repository.TeamRepository;
import com.mikul17.bazyDanych.Request.TeamRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamService.class);
    private final TeamRepository teamRepository;
    private final LeagueService leagueService;

    public Team createTeam(TeamRequest request) {
        try{
            Team team = Team.builder()
                    .teamName(request.getTeamName())
                    .league(leagueService.getLeagueById(request.getLeagueId()))
                    .wins(0)
                    .draws(0)
                    .loses(0)
                    .goalsScored(0)
                    .goalsConceded(0)
                    .leaguePoints(0)
                    .build();
            log.info("Team created: {} in league : {}", team.getTeamName(), team.getLeague().getLeagueName());
            return teamRepository.save(team);
        }catch (Exception e){
            throw new ServiceException("Error while adding team: "+e.getMessage());
        }
    }
    public List<Team> createTeamsFromList(List<TeamRequest> teams) {
        try{
            List<Team> teamList = new ArrayList<>();
            for (TeamRequest teamRequest : teams) {
                Team team = Team.builder()
                        .teamName(teamRequest.getTeamName())
                        .league(leagueService.getLeagueById(teamRequest.getLeagueId()))
                        .loses(0)
                        .wins(0)
                        .draws(0)
                        .loses(0)
                        .goalsScored(0)
                        .goalsConceded(0)
                        .leaguePoints(0)
                        .build();
                teamRepository.save(team);
                teamList.add(team);
                log.info("Team created: {} in league : {}", team.getTeamName(), team.getLeague().getLeagueName());
            }
            return teamList;
        } catch (Exception e) {
            throw new ServiceException("Error while creating teams from list: "+e.getMessage());
        }
    }
    public List<Team> getTeamsByLeagueId(Long id) {
        try{
            return teamRepository.findAllByLeagueId(id);
        }catch (Exception e){
            throw new ServiceException("Error while getting teams by league id", e);
        }
    }
    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Team not found"));
    }
    public List<Team> getAllTeams() {
        try{
            return teamRepository.findAll();
        }catch (Exception e){
            throw new ServiceException("Error while getting teams", e);
        }
    }
    public void deleteTeam(Long id) {
        try{
            teamRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Team not found"));
            teamRepository.deleteById(id);
        }catch (Exception e){
           throw new ServiceException("Error while deleting team: "+e.getMessage());
        }
    }
    public void deleteAllTeams() {
        try{
            teamRepository.deleteAll();
        }catch (Exception e){
            throw new ServiceException("Error while deleting teams: "+e.getMessage());
        }
    }
    public Team resetTeamStats(Long id) {
        try{
            Team team = getTeamById(id);
            team.setWins(0);
            team.setDraws(0);
            team.setLoses(0);
            team.setGoalsScored(0);
            team.setGoalsConceded(0);
            team.setLeaguePoints(0);
            log.info("Team stats reset: {}", team.getTeamName());
            return teamRepository.save(team);
        }catch (Exception e){
           throw new ServiceException("Error while reseting team: "+e.getMessage());
        }
    }
    public void deleteTeamByTeamName(String teamName) {
        try{
            Team team = teamRepository.findByTeamName(teamName).orElseThrow(()
                    -> new ServiceException("Team not found"));
            teamRepository.delete(team);
        }catch (Exception e){
           throw new ServiceException("Error while deleting team by teamName: "+e.getMessage());
        }
    }
    public void resetTeamStatsForWholeLeague(League league){
        try{
            List<Team> teams = getTeamsByLeagueId(league.getId());
            for(Team team : teams){
                resetTeamStats(team.getId());
            }
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public String getTeamNameById(Long teamId) {
        try{
            Team team = teamRepository.findById(teamId).orElseThrow(()->
                    new ServiceException("Team with given id doesn't exist"));

            return team.getTeamName();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public List<Team> getTeamsByLeagueOrderByPoints (Long leagueId) {
        try{
            return teamRepository.findAllByLeagueIdOrderByPointsAndGoalDifference(leagueId);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public Integer getDifferenceInRelativePositionBetweenTeams(Team first , Team second){
        if(!Objects.equals(first.getLeague().getId(), second.getLeague().getId())){
            throw new ServiceException("Teams are not from the same league");
        }
        List<Team> ordered = getTeamsByLeagueOrderByPoints(first.getLeague().getId());
        int firstTeamIndex = ordered.indexOf(first);
        int secondTeamIndex = ordered.indexOf(second);

        return Math.abs(firstTeamIndex-secondTeamIndex);
    }

    public Integer getRelativePositionInLeagueTable(Team team){
        List<Team> ordered = getTeamsByLeagueOrderByPoints(team.getLeague().getId());
        return ordered.indexOf(team);
    }
}
