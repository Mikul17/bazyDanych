package com.mikul17.bazyDanych.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mikul17.bazyDanych.Request.BetRequest;
import com.mikul17.bazyDanych.Service.BetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bet")
public class BetController {

    private final BetService betService;

    @PostMapping("/placeBet")
    public ResponseEntity<?> createBet(@RequestBody BetRequest betRequest) {
        try {
            return ResponseEntity.ok().body(betService.createBet(betRequest));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBet(@PathVariable Long id, @RequestBody BetRequest betRequest) {
        try {
            return ResponseEntity.ok().body(betService.updateBet(id, betRequest));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBets() {
        try{
            return ResponseEntity.ok().body(betService.getAllBets());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBetById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok().body(betService.getBetById(id));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<?> getBetsByMatchId(@PathVariable Long matchId) {
        try{
            return ResponseEntity.ok().body(betService.getBetsByMatchId(matchId));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/check/{id}")
    public ResponseEntity<?> checkBet(@PathVariable Long id, @RequestBody JsonNode jsonNode) {
        try{
            int betStatus = jsonNode.get("betStatus").asInt();
            return ResponseEntity.ok().body(betService.changeBetStatus(id, betStatus));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBet(@PathVariable Long id) {
        try{
            betService.deleteBet(id);
            return ResponseEntity.ok().body("Bet with id: "+id+" deleted");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
