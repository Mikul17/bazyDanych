package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Service.PlayerStatService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/player_stat")
public class PlayerStatController {

    private final PlayerStatService playerStatService;
    private final Logger logger = LoggerFactory.getLogger(PlayerStatController.class);

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerStatById(@PathVariable Long id){
       try{
           return ResponseEntity.ok(playerStatService.getPlayerStatById(id));
       }catch (ServiceException e){
           logger.error(e.getMessage());
           return ResponseEntity.badRequest().body(e.getMessage());
       }catch (Exception e){
           logger.error(e.getMessage());
           return ResponseEntity.internalServerError().body(e.getMessage());
       }
    }
}
