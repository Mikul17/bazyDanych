package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.MatchEventRequest;
import com.mikul17.bazyDanych.Service.MatchEventService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matchEvent")
public class MatchEventController {

    private final MatchEventService matchEventService;

    @PostMapping("/add")
    public ResponseEntity<?> addEvent(@RequestBody MatchEventRequest request){
        try{
            return ResponseEntity.ok().body(matchEventService.addEvent(request));
        }catch (ServiceException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/all/{matchId}")
    public ResponseEntity<?> getAll(@PathVariable Long matchId){
        try{
            return ResponseEntity.ok().body(matchEventService.getAllEventsAsString(matchId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
