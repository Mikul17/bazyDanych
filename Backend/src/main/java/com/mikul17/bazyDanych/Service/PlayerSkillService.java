package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Simulation.Players.PlayerSkill;
import com.mikul17.bazyDanych.Repository.PlayerRepository;
import com.mikul17.bazyDanych.Repository.PlayerSkillRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerSkillService {

    private final PlayerSkillRepository playerSkillRepository;
    private final PlayerRepository playerRepository;

    public PlayerSkill getAllPlayerSkills(Long id) {
        try{
            playerRepository.findById(id).orElseThrow(() -> new ServiceException(
                    "Player with id: " + id + " does not exist"));
            return playerSkillRepository.findById(id)
                    .orElseThrow(() -> new ServiceException(
                            "Skills for player with id: " + id + " does not exist"));
        }catch (Exception e){
            throw new ServiceException("Error while getting player stats: "+e.getMessage());
        }
    }
}
