package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Repository.PlayerRepository;
import com.mikul17.bazyDanych.Repository.PlayerSkillRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerSkillService {

    private final Logger log = LoggerFactory.getLogger(PlayerSkillService.class);
    private final PlayerSkillRepository playerSkillRepository;
    private final PlayerRepository playerRepository;

    public ResponseEntity<?> getAllPlayerSkills(Long id) {
       try{
              if(playerRepository.findById(id).isPresent()){
                return ResponseEntity.ok(playerSkillRepository.findById(id).get());
              }else{
                return ResponseEntity.badRequest().body("Player with id: " + id + " does not exist");
              }
       }catch (Exception e){
           log.error("Error while getting all player skills: " + e.getMessage());
              return ResponseEntity.badRequest().body("Player with id: " + id + " does not exist");
       }
    }
}
