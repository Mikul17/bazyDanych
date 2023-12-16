package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.MatchRequest;
import com.mikul17.bazyDanych.Service.MatchService;
import com.mikul17.bazyDanych.Simulation.MatchScheduler;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match")
public class MatchController {

    private final Logger logger = LoggerFactory.getLogger(MatchController.class);
    private final MatchService matchService;
    private final MatchScheduler matchScheduler;

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
            return ResponseEntity.ok().body(matchService.getUpcomingMatches());
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMatchById(@PathVariable Long id){
        try{
            matchService.deleteMatch(id);
            logger.info("Match with id:{} deleted",id);
            return ResponseEntity.ok().body("Match deleted successfully");
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PutMapping("/updateDate/{id}")
    public ResponseEntity<?> updateMatchDate(@PathVariable Long id, @RequestBody Timestamp date){
        try{

            return ResponseEntity.ok().body(matchService.updateMatchDate(id,date));
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/test1")
    public ResponseEntity<?> testMethod() throws Exception {
        matchScheduler.scheduleMatchesForSeason();
        return ResponseEntity.ok().body(matchService.getUpcomingMatches());
    }
}
