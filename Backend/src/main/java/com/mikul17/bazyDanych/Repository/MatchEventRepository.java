package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Matches.MatchEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvents, Long> {

    public List<MatchEvents> findMatchEventsByMatchOrderByMinuteAsc(Match match);
}
