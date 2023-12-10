package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Simulation.Players.PlayerStat;
import com.mikul17.bazyDanych.Repository.PlayerRepository;
import com.mikul17.bazyDanych.Repository.PlayerStatRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerStatService {

    private final PlayerStatRepository playerStatRepository;
    private final PlayerRepository playerRepository;


    public PlayerStat getPlayerStatById(Long id){
        try{
            playerRepository.findById(id).orElseThrow(() -> new ServiceException(
                    "Player with id: " + id + " does not exist"));
            return playerStatRepository.findById(id)
                    .orElseThrow(() -> new ServiceException(
                            "Stats for player with id: " + id + " does not exist"));
        }catch (Exception e){
           throw new ServiceException("Error while getting player stats: "+e.getMessage());
        }
    }
}
