package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Simulation.Players.Player;
import com.mikul17.bazyDanych.Models.Simulation.Players.PlayerSkill;
import com.mikul17.bazyDanych.Models.Simulation.Players.PlayerStat;
import com.mikul17.bazyDanych.Repository.PlayerRepository;
import com.mikul17.bazyDanych.Repository.PlayerSkillRepository;
import com.mikul17.bazyDanych.Repository.PlayerStatRepository;
import com.mikul17.bazyDanych.Request.PlayerRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final PlayerSkillRepository playerSkillRepository;
    private final PlayerStatRepository playerStatRepository;

    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok().body(playerRepository.findAll());
    }
    public ResponseEntity<?> getPlayerById(Long id) {
       try{
           return ResponseEntity.ok().body(playerRepository.findById(id).orElseThrow(() -> new Exception("Player not found")));
       } catch (Exception e) {
           logger.error("Error with getting player:{} ",e.getMessage());
           return ResponseEntity.badRequest().body("Player not found");
       }
    }
    @Transactional
    public ResponseEntity<?> addPlayer(PlayerRequest request){
        try{
            Player player = new Player();
            player.setFirstName(request.getFirstName());
            player.setLastName(request.getLastName());
            player.setAge(request.getAge());
            player.setNationality(request.getNationality());
            player.setPosition(request.getPosition());
            player = playerRepository.save(player);

            PlayerSkill playerSkill = request.getPlayerSkill();
            if (playerSkill != null) {
                playerSkill.setPlayer(player);
                playerSkillRepository.save(playerSkill);
            }

            PlayerStat playerStat = new PlayerStat();
            playerStat.setPlayer(player);
            playerStatRepository.save(playerStat);

            return ResponseEntity.ok().body(player);
        } catch (Exception e) {
            logger.error("Error with adding player:{} ",e.getMessage());
            return ResponseEntity.badRequest().body("Error with adding player: " + e.getMessage());
        }
    }
    @Transactional
    public ResponseEntity<?> deletePlayer(Long id) {
        try{
            playerRepository.findById(id).orElseThrow(() -> new Exception("Player not found"));
            playerSkillRepository.deleteById(id);
            playerStatRepository.deleteById(id);
            playerRepository.deleteById(id);
            return ResponseEntity.ok().body("Player deleted");
        } catch (Exception e) {
            logger.error("Error with deleting player:{} ",e.getMessage());
            return ResponseEntity.badRequest().body("Error with deleting player: " + e.getMessage());
        }
    }
}