package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Service.MatchStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matchStat")
public class MatchStatsController {

    private final MatchStatsService matchStatsService;

    @GetMapping("/all/homeTeam/{matchId}")
    public ResponseEntity<?> getHomeTeamStats(@PathVariable Long matchId){
        try{
            return ResponseEntity.ok().body(matchStatsService.getAllStatsForTeam(matchId,true));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all/awayTeam/{matchId}")
    public ResponseEntity<?> getAwayTeamStats(@PathVariable Long matchId){
        try{
            return ResponseEntity.ok().body(matchStatsService.getAllStatsForTeam(matchId,false));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
