package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.LeagueRequest;
import com.mikul17.bazyDanych.Service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/league")
public class LeagueController {

    public final LeagueService leagueService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllLeagues() {
        return leagueService.getAllLeagues();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLeagueById(@PathVariable Long id) {
        return leagueService.getLeagueById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLeague(@RequestBody LeagueRequest request) {
        return leagueService.addLeague(request);
    }
    @PostMapping("/addLeagues")
    public ResponseEntity<?> addLeagues(@RequestBody List<LeagueRequest> request) {
        return leagueService.addLeagues(request);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLeague(@PathVariable Long id) {
        return leagueService.deleteLeague(id);
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllLeagues() {
        return leagueService.deleteAll();
    }
}
