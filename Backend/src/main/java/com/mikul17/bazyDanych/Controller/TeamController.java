package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.TeamRequest;
import com.mikul17.bazyDanych.Service.TeamService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;

    @PostMapping("/add")
    public ResponseEntity<?> createTeam(@RequestBody TeamRequest request){
        try{
            return ResponseEntity.ok(teamService.createTeam(request));
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            log.error("Error "+HttpStatus.INTERNAL_SERVER_ERROR+" while creating team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/addTeams")
    public ResponseEntity<?> createTeamsFromList(@RequestBody List<TeamRequest> teams) {
        try{
            return ResponseEntity.ok(teamService.createTeamsFromList(teams));
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            log.error("Error " + HttpStatus.INTERNAL_SERVER_ERROR +" while creating teams from list: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTeams() {
        try{
            return ResponseEntity.ok(teamService.getAllTeams());
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            log.error("Error "+HttpStatus.INTERNAL_SERVER_ERROR+" while getting teams: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable Long id) throws Exception {
        try{
            return ResponseEntity.ok(teamService.getTeamById(id));
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            log.error("Error "+HttpStatus.INTERNAL_SERVER_ERROR+" while getting team: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getByLeague/{id}")
    public ResponseEntity<?> getTeamsByLeagueId(@PathVariable Long id) {
        try{
            log.info("Teams list received");
            return ResponseEntity.ok(teamService.getTeamsByLeagueId(id));
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            log.error("Error "+HttpStatus.INTERNAL_SERVER_ERROR+" while getting teams: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) throws Exception {
        try{
            teamService.deleteTeam(id);
            log.info("Team deleted: {}", id);
            return ResponseEntity.ok("Deleted team with id: "+id);
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            log.error("Error "+HttpStatus.INTERNAL_SERVER_ERROR+" while deleting team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllTeams() {
       try{
           teamService.deleteAllTeams();
           log.info("All teams deleted");
           return ResponseEntity.ok("All teams deleted");
       }catch (ServiceException e){
           log.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }catch (Exception e){
           log.error("Error "+HttpStatus.INTERNAL_SERVER_ERROR+" while deleting all teams: {}", e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
       }
    }
    @DeleteMapping("/deleteByTeamName/{teamName}")
    public ResponseEntity<?> deleteTeamByTeamName(@PathVariable String teamName) {
        try{
            teamService.deleteTeamByTeamName(teamName);
            log.info("Team deleted: {}", teamName);
            return ResponseEntity.ok("Team "+teamName+" deleted successfully");
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            log.error("Error "+HttpStatus.INTERNAL_SERVER_ERROR+" while deleting team "+teamName+": {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/reset/{id}")
    public ResponseEntity<?> resetTeamStats(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(teamService.resetTeamStats(id));
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            log.error("Error "+HttpStatus.INTERNAL_SERVER_ERROR+" while resetting team stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/all/league/order/{leagueId}")
    public ResponseEntity<?> getAllByLeagueOrderByPoints(@PathVariable Long leagueId){
        try{
            return ResponseEntity.ok().body(teamService.getTeamsByLeagueOrderByPoints(leagueId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
