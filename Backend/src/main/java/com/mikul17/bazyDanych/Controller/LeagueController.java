package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.LeagueRequest;
import com.mikul17.bazyDanych.Service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/league")
public class LeagueController {

    private static final Logger log = LoggerFactory.getLogger(LeagueController.class);
    public final LeagueService leagueService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllLeagues() {
        try{
            return ResponseEntity.ok(leagueService.getAllLeagues());
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLeagueById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(leagueService.getLeagueById(id));
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLeague(@RequestBody LeagueRequest request) {
        try{
            return ResponseEntity.ok(leagueService.addLeague(request));
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("/addLeagues")
    public ResponseEntity<?> addLeagues(@RequestBody List<LeagueRequest> request) {
        try{
            return ResponseEntity.ok(leagueService.addLeagues(request));
        }catch (ServiceException e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
