package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.PlayerRequest;
import com.mikul17.bazyDanych.Service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/player")
public class PlayerController {

    private final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService playerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPlayers() {
       try{
           return ResponseEntity.ok().body(playerService.getAllPlayers());
       }catch (ServiceException e){
           logger.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
       }catch (Exception e){
           logger.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
       }
    }

    @GetMapping("/all/{teamId}")
    public ResponseEntity<?> getAllPlayersByTeam(@PathVariable Long teamId){
        try {
            return ResponseEntity.ok().body(playerService.getAllPlayerByTeam(teamId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(playerService.getPlayerById(id));
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            logger.error("Error with getting player:{} ",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPlayer(@RequestBody PlayerRequest request) {
        try{
           return ResponseEntity.ok(playerService.addPlayer(request));
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        try{
            playerService.deletePlayer(id);
            return ResponseEntity.ok().body("Player deleted");
        }catch (ServiceException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
