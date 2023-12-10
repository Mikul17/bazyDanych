package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Service.PlayerSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/player_skill")
public class PlayerSkillController {

    private final PlayerSkillService playerSkillService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllPlayerSkills(@PathVariable Long id) {
        return playerSkillService.getAllPlayerSkills(id);
    }

}
