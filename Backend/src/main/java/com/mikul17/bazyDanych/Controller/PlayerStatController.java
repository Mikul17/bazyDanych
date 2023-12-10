package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Service.PlayerStatService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerStatById(@PathVariable Long id){
        return playerStatService.getPlayerStatById(id);
    }
}
