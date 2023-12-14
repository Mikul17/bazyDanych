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
    private final TeamService teamService;

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
            Player player = Player.builder()
                    .team(teamService.getTeamById(request.getTeamId()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .age(request.getAge())
                    .nationality(request.getNationality())
                    .position(request.getPosition())
                    .build();
            player = playerRepository.save(player);

            PlayerSkill playerSkill = request.getPlayerSkill();
            if (playerSkill != null) {
                playerSkill.setPlayer(player);
                playerSkillRepository.save(playerSkill);
            }

            PlayerStat playerStat = PlayerStat.builder()
                    .player(player)
                    .goalsScored(0)
                    .assists(0)
                    .yellowCards(0)
                    .redCards(0)
                    .fouls(0)
                    .gamesPlayed(0)
                    .build();
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