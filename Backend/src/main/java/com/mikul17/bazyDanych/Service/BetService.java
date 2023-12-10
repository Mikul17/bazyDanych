package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Repository.BetRepository;
import com.mikul17.bazyDanych.Repository.BetTypeRepository;
import com.mikul17.bazyDanych.Repository.MatchRepository;
import com.mikul17.bazyDanych.Request.BetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BetService {

    public final BetRepository betRepository;
    public final BetTypeRepository betTypeRepository;
    public final MatchRepository matchRepository;

    //Utility methods


    //C - Create
    public ResponseEntity<Bet> addBet(BetRequest betRequest) {
        try{
            Match refMatch = matchRepository.findById(betRequest.getMatchId()).orElseThrow(() -> new RuntimeException("Match not found"));
            BetType refBetType = betTypeRepository.findById(betRequest.getBetTypeId()).orElseThrow(() -> new RuntimeException("BetType not found"));
            Bet bet = new Bet().builder()
                    .match(refMatch)
                    .odds(betRequest.getOdds())
                    .betStatus(0)
                    .betType(refBetType)
                    .build();
            betRepository.save(bet);
            return ResponseEntity.ok(bet);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //R - Read
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }
    public Bet getBetById(Long id) {
        return betRepository.findById(id).orElseThrow(() -> new RuntimeException("Bet not found"));
    }

    //U - Update
    public ResponseEntity<String> updateBetOdds(Long betId, Double odds) {
        try{
            Bet bet = betRepository.findById(betId).orElseThrow(() -> new RuntimeException("Bet not found"));
            bet.setOdds(odds);
            betRepository.save(bet);
            return ResponseEntity.status(HttpStatus.OK).body("Bet updated successfully - new odds: " + odds);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error while updating bet: " + e.getMessage());
        }
    }
    public ResponseEntity<String> updateBetOdds(Long betId){
        //TO-DO add logic to update odds based on how many bets are placed for that team
        return null;
    }
    //D - Delete
    public ResponseEntity<String> deleteBet(Long id) {
        try{
            betRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Bet deleted successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error while deleting bet: " + e.getMessage());
        }
    }
}
