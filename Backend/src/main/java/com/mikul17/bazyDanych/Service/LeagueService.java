package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Simulation.League;
import com.mikul17.bazyDanych.Repository.LeagueRepository;
import com.mikul17.bazyDanych.Request.LeagueRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private static final Logger log = LoggerFactory.getLogger(LeagueService.class);
    private final LeagueRepository leagueRepository;

    public ResponseEntity<?> getAllLeagues() {
        return ResponseEntity.ok(leagueRepository.findAll());
    }
    public ResponseEntity<?> getLeagueById(Long id) {
        try{
            leagueRepository.findById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
        return ResponseEntity.ok(leagueRepository.findById(id));
    }
    public ResponseEntity<?> addLeague(LeagueRequest leagueRequest) {
        try{
            League league = League.builder()
                    .leagueName(leagueRequest.getLeagueName())
                    .country(leagueRequest.getCountry())
                    .season(0)
                    .build();
            leagueRepository.save(league);
            log.info("League: "+league.getLeagueName()+" created successfully");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("League: "+league.getLeagueName()+" created successfully");
        } catch (Exception e) {
            log.error("Error while creating league: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    public ResponseEntity<?> addLeagues (List<LeagueRequest> request) {
        try{
            for (LeagueRequest leagueRequest : request) {
                League league = League.builder()
                        .leagueName(leagueRequest.getLeagueName())
                        .country(leagueRequest.getCountry())
                        .build();
                leagueRepository.save(league);
            }
            log.info("Leagues created successfully");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Leagues created successfully");
        } catch (Exception e) {
            log.error("Error while creating teams: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
