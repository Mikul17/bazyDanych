package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.MatchRequest;
import com.mikul17.bazyDanych.Service.MatchService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match")
public class MatchController {

    private final Logger logger = LoggerFactory.getLogger(MatchController.class);
    private final MatchService matchService;

    @PostMapping("/add")
    public ResponseEntity<?> addMatch(@RequestBody MatchRequest request){
        try{
            return ResponseEntity.ok(matchService.addMatch(request));
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcoming(){
        try{
            return ResponseEntity.ok().body(matchService.getUpcomingMatchResponse());
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/todays")
    public ResponseEntity<?> getToday(){
        try{
            return ResponseEntity.ok().body(matchService.getTodayMatches());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tomorrows")
    public ResponseEntity<?> getTomorrow(){
        try{
            return ResponseEntity.ok().body(matchService.getTomorrowMatches());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/getByTeamPlayed/{id}")
    public ResponseEntity<?> getPlayedMatchesByTeam(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(matchService.getMatchesByTeam(id,false));
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/getByTeamUpcoming/{id}")
    public ResponseEntity<?> getUpcomingMatchesByTeam(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(matchService.getMatchesByTeam(id,true));
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/today/{leagueId}")
    public ResponseEntity<?> todayMatchesByLeague (@PathVariable Long leagueId){
        try{
            return ResponseEntity.ok().body(matchService.getTodayMatchesByLeague(leagueId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/upcoming/{leagueId}")
    public ResponseEntity<?> upcomingMatchesByLeague(@PathVariable Long leagueId){
        try{
            return ResponseEntity.ok().body(matchService.getUpcomingMatchesByLeague(leagueId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history/")
    public ResponseEntity<?> getTeamHistoryById(
            @RequestParam("matchId")Optional<Long> matchId,
            @RequestParam("isHomeTeam") Optional<Boolean> isHomeTeam){
        try{
            return ResponseEntity.ok().body(matchService.getMatchHistoryByMatch(matchId,isHomeTeam));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getMatchResponseById(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(matchService.getMatchResponseById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
