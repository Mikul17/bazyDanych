package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Matches.MatchEvents;
import com.mikul17.bazyDanych.Models.Simulation.Players.Player;
import com.mikul17.bazyDanych.Repository.MatchEventRepository;
import com.mikul17.bazyDanych.Request.MatchEventRequest;
import com.mikul17.bazyDanych.Response.MatchEventResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchEventService {

    private final MatchEventRepository matchEventRepository;
    private final PlayerService playerService;
    private final MatchService matchService;


    public String addEvent(MatchEventRequest request){
        try{
            Player player = playerService.getPlayerById(request.getPlayerId());
            Match match = matchService.getMatchById(request.getMatchId());

            MatchEvents event = MatchEvents.builder()
                    .match(match)
                    .player(player)
                    .minute(request.getMinute())
                    .actionType(request.getActionType())
                    .actionDescription(request.getActionDescription())
                    .build();
            matchEventRepository.save(event);
            return event.getActionDescription();

        }catch (Exception e){
            throw new ServiceException("Error: "+e.getMessage());
        }
    }

    public List<MatchEventResponse> getAllEventsAsString(Long matchId){
        try{
            Match match = matchService.getMatchById(matchId);
            List<MatchEvents> events = matchEventRepository.findMatchEventsByMatchOrderByMinuteAsc(match);
            List<MatchEventResponse> eventString = new ArrayList<>();
            boolean isHomeTeam=true;
            for(MatchEvents event : events){
                if(event.getPlayer()!=null){
                    isHomeTeam = event.getPlayer().getTeam().getId().equals(match.getHomeTeam().getId());
                }
                String s = "[" +
                        event.getMinute() +
                        "] : " +
                        event.getActionDescription();

                eventString.add(MatchEventResponse.builder()
                                .id(event.getId())
                                .actionType(event.getActionType())
                                .desc(s)
                                .isHomeTeam(isHomeTeam)
                                .build());
            }
            return eventString;
        }catch (Exception e){
            throw new ServiceException("Error: "+e.getMessage());
        }
    }
}
