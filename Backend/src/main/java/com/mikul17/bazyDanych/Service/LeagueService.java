package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.League;
import com.mikul17.bazyDanych.Repository.LeagueRepository;
import com.mikul17.bazyDanych.Request.LeagueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeagueService {

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
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("League: "+league.getLeagueName()+" created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
