package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.TeamRequest;
import com.mikul17.bazyDanych.Service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/add")
    public ResponseEntity<?> createTeam(@RequestBody TeamRequest request) throws Exception {
        return teamService.createTeam(request);
    }

    @PostMapping("/addTeams")
    public ResponseEntity<?> createTeamsFromList(@RequestBody List<TeamRequest> teams) {
        return teamService.createTeamsFromList(teams);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable Long id) throws Exception {
        return teamService.getTeamById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) throws Exception {
        return teamService.deleteTeam(id);
    }

    @PutMapping("/reset/{id}")
    public ResponseEntity<?> resetTeamStats(@PathVariable Long id) throws Exception {
        return teamService.resetTeamStats(id);
    }

    @PutMapping("/updateCaptain/{id}")
    public ResponseEntity<?> updateTeamCaptain(@PathVariable Long id, @RequestBody Long playerId) throws Exception {
        return teamService.updateTeamCaptain(id, playerId);
    }
}
