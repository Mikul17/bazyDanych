package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Repository.PlayerRepository;
import com.mikul17.bazyDanych.Repository.PlayerStatRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerStatService {

    private final PlayerStatRepository playerStatRepository;
    private final PlayerRepository playerRepository;
    private final Logger logger = LoggerFactory.getLogger(PlayerStatService.class);

    public ResponseEntity<?> getPlayerStatById(Long id){
        try{
            return ResponseEntity.ok(playerStatRepository.findById(id)
                    .orElseThrow(() -> new Exception(
                            "Player with id: " + id + " does not exist")));
        }catch (Exception e){
            logger.error("Error while getting player stat: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while getting player stat: "+e.getMessage());
        }
    }
}
