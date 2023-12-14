package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Simulation.League;
import com.mikul17.bazyDanych.Repository.LeagueRepository;
import com.mikul17.bazyDanych.Request.LeagueRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final Logger log = LoggerFactory.getLogger(LeagueService.class);
    private final LeagueRepository leagueRepository;

    public List<League> getAllLeagues() {
        try{
            return leagueRepository.findAll();
        }catch (Exception e){
            throw new ServiceException("Error while getting teams: "+e.getMessage());
        }
    }
    public League getLeagueById(Long id) {
        try{
            return leagueRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Couldn't find league with id: "+id));
        } catch (Exception e) {
          throw new ServiceException("Error while getting league: "+e.getMessage());
        }
    }
    public League addLeague(LeagueRequest leagueRequest) {
        try{
            League league = League.builder()
                    .leagueName(leagueRequest.getLeagueName())
                    .country(leagueRequest.getCountry())
                    .season(0)
                    .build();
            leagueRepository.save(league);
            log.info("League: "+league.getLeagueName()+" created successfully");
            return league;
        } catch (Exception e) {
            throw new ServiceException("Error while adding league: "+e.getMessage());
        }
    }
    public List<League> addLeagues (List<LeagueRequest> request) {
        try{
            List<League> addedLeagues = new ArrayList<>();
            for (LeagueRequest leagueRequest : request) {
                League league = League.builder()
                        .leagueName(leagueRequest.getLeagueName())
                        .country(leagueRequest.getCountry())
                        .build();
                leagueRepository.save(league);
                addedLeagues.add(league);
            }
            log.info("Leagues created successfully");
            return addedLeagues;
        } catch (Exception e) {
            throw new ServiceException("Error while adding leagues: "+e.getMessage());
        }
    }
    public void deleteLeague(Long id) {
        try{
            leagueRepository.findById(id).
                    orElseThrow(()->new ServiceException("League with id: "+id+" doesn't exist"));
            leagueRepository.deleteById(id);
            log.info("League with id: "+id+" deleted successfully");
        } catch (Exception e) {
            throw new ServiceException("Error while deleting league: "+e.getMessage());
        }
    }
    public void deleteAll(){
        try{
            leagueRepository.deleteAll();
            log.info("All leagues deleted successfully");
        } catch (Exception e) {
            throw new ServiceException("Error while deleting all leagues: "+e.getMessage());
        }
    }
}
