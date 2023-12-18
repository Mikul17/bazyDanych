package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Matches.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    List<Bet> findByMatch (Match match);
    List<Bet> findByMatchAndBetType_betStat (Match match, String stat);
    List<Bet> findByMatchAndBetType_BetStatAndBetType_Team(Match match, String betStat, Integer team);
    List<Bet> findByMatchAndBetType_BetStatAndBetType_TargetValue(Match match, String betType_betStat, Double betType_targetValue);
    List<Bet> findByMatchAndBetType_BetStatAndBetType_TeamAndBetType_TargetValueNot(Match match, String betStat, Integer team, Double targetValue);
}
