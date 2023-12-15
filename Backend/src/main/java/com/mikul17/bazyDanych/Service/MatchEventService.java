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


    public MatchEventResponse addEvent(MatchEventRequest request){
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
            return mapMatchEventToMatchEventResponse(event);

        }catch (Exception e){
            throw new ServiceException("Error: "+e.getMessage());
        }
    }

    public List<String> getAllEventsAsString(Long matchId){
        try{
            Match match = matchService.getMatchById(matchId);
            List<MatchEvents> events = matchEventRepository.findMatchEventsByMatch(match);
            List<String> eventString = new ArrayList<>();
            for(MatchEvents event : events){
                String s = "[" +
                        event.getMinute() +
                        "] :" +
                        event.getActionDescription();
                eventString.add(s);
            }
            return eventString;
        }catch (Exception e){
            throw new ServiceException("Error: "+e.getMessage());
        }
    }

    private MatchEventResponse mapMatchEventToMatchEventResponse(MatchEvents event){
        try{
            return MatchEventResponse.builder()
                    .matchId(event.getMatch().getId())
                    .playerId(event.getPlayer().getId())
                    .minute(event.getMinute())
                    .actionDescription(event.getActionDescription())
                    .actionType(event.getActionType())
                    .build();
        }catch (Exception e){
            throw new ServiceException("Error: "+e.getMessage());
        }
    }
}
