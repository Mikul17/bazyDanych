package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Team;
import com.mikul17.bazyDanych.Repository.LeagueRepository;
import com.mikul17.bazyDanych.Repository.TeamRepository;
import com.mikul17.bazyDanych.Request.TeamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;

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
            return ResponseEntity.ok(teamRepository.save(team));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity<?> getTeam(Long id) throws Exception {
        try{
            return ResponseEntity.ok(teamRepository.findById(id).orElseThrow(()
                    -> new Exception("Team not found")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
