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
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {


    private final PlayerRepository playerRepository;
    private final PlayerSkillRepository playerSkillRepository;
    private final PlayerStatRepository playerStatRepository;

    public List<Player> getAllPlayers() {
        try{
            return playerRepository.findAll();
        }catch (Exception e){
            throw new ServiceException("Error while getting players: "+e.getMessage());
        }
    }
    public Player getPlayerById(Long id) {
       try{
           return playerRepository.findById(id).orElseThrow(() -> new ServiceException("Player not found"));
       } catch (Exception e) {
           throw new ServiceException("Error while getting player: "+e.getMessage());
       }
    }
    @Transactional
    public Player addPlayer(PlayerRequest request){
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

            return player;
        } catch (Exception e) {
          throw new ServiceException("Error while adding new player: "+e.getMessage());
        }
    }
    @Transactional
    public void deletePlayer(Long id) {
        try{
            playerRepository.findById(id).orElseThrow(() -> new ServiceException("Player not found"));
            playerSkillRepository.deleteById(id);
            playerStatRepository.deleteById(id);
            playerRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error with deleting player: " + e.getMessage());
        }
    }
}