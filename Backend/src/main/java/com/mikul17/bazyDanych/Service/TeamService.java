package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Repository.PlayerRepository;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import com.mikul17.bazyDanych.Repository.LeagueRepository;
import com.mikul17.bazyDanych.Repository.TeamRepository;
import com.mikul17.bazyDanych.Request.TeamRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;
    private final PlayerRepository playerRepository;

    public ResponseEntity<?> createTeam(TeamRequest request) throws Exception {
        try{
            Team team = Team.builder()
                    .teamName(request.getTeamName())
                    .league(leagueRepository.findById(request.getLeagueId()).orElseThrow(()
                            -> new Exception("League not found")))
                    .wins(0)
                    .draws(0)
                    .loses(0)
                    .goalsScored(0)
                    .goalsConceded(0)
                    .leaguePoints(0)
                    .build();
            log.info("Team created: {} in league : {}", team.getTeamName(), team.getLeague().getLeagueName());
            return ResponseEntity.ok(teamRepository.save(team));
        }catch (Exception e){
            log.error("Error while creating team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    public ResponseEntity<?> createTeamsFromList(List<TeamRequest> teams) {
        try{
            for (TeamRequest teamRequest : teams) {
                Team team = Team.builder()
                        .teamName(teamRequest.getTeamName())
                        .league(leagueRepository.findById(teamRequest.getLeagueId()).orElseThrow(()
                                -> new Exception("League not found")))
                        .wins(0)
                        .draws(0)
                        .loses(0)
                        .goalsScored(0)
                        .goalsConceded(0)
                        .leaguePoints(0)
                        .build();
                teamRepository.save(team);
                log.info("Team created: {} in league : {}", team.getTeamName(), team.getLeague().getLeagueName());
            }
            return ResponseEntity.ok(teams.size()+" teams created !");
        } catch (Exception e) {
            log.error("Error while creating teams from list: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    public ResponseEntity<?> getTeamsByLeagueId(Long id) throws Exception {
        try{
            return ResponseEntity.ok(teamRepository.findAllByLeagueId(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    public ResponseEntity<?> getTeamById(Long id) throws Exception {
        try{
            return ResponseEntity.ok(teamRepository.findById(id).orElseThrow(()
                    -> new Exception("Team not found")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    public ResponseEntity<?> getAllTeams() {
        return ResponseEntity.ok(teamRepository.findAll());
    }
    public ResponseEntity<?> deleteTeam(Long id) throws Exception {
        try{
            teamRepository.findById(id).orElseThrow(()
                    -> new Exception("Team not found"));
            teamRepository.deleteById(id);
            log.info("Team deleted: {}", id);
            return ResponseEntity.ok("Team deleted");
        }catch (Exception e){
            log.error("Error while deleting team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    public ResponseEntity<?> resetTeamStats(Long id) throws Exception {
        try{
            Team team = teamRepository.findById(id).orElseThrow(()
                    -> new Exception("Team not found"));
            team.setWins(0);
            team.setDraws(0);
            team.setLoses(0);
            team.setGoalsScored(0);
            team.setGoalsConceded(0);
            team.setLeaguePoints(0);
            log.info("Team stats reset: {}", team.getTeamName());
            return ResponseEntity.ok(teamRepository.save(team));
        }catch (Exception e){
            log.error("Error while resetting team stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    public ResponseEntity<?> deleteAllTeams() {
        try{
            teamRepository.deleteAll();
            log.info("All teams deleted");
            return ResponseEntity.ok("All teams deleted");
        }catch (Exception e){
            log.error("Error while deleting all teams: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    public ResponseEntity<?> deleteTeamByTeamName(String teamName) throws Exception {
        try{
            Team team = teamRepository.findByTeamName(teamName).orElseThrow(()
                    -> new Exception("Team not found"));
            teamRepository.delete(team);
            log.info("Team deleted: {}", teamName);
            return ResponseEntity.ok("Team "+teamName+" deleted successfully");
        }catch (Exception e){
            log.error("Error while deleting team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private ResponseEntity<?> updateTeamCaptain(Long id, Long playerId) throws Exception {
        try{
            Team team = teamRepository.findById(id).orElseThrow(()
                    -> new Exception("Team not found"));
            team.setCaptain(playerRepository.findById(playerId).orElseThrow(()
                    -> new Exception("Player not found")));
            log.info("New captain set: {} for team: {}", team.getCaptain().getLastName(), team.getTeamName());
            return ResponseEntity.ok(teamRepository.save(team));
        }catch (Exception e){
            log.error("Error while setting new captain: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
