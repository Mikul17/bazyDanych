package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Coupons.BetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BetTypeRepository extends JpaRepository<BetType, Long>{
    Optional<BetType> findById(Long id);
    List<BetType> findByBetTypeCode(String betTypeCode);
    List<BetType> findByBetStat(String betStat);
    List<BetType> findByBetTypeCodeAndBetStatOrderByTargetValueAsc(String betTypeCode, String betStat);
    List<BetType> findByBetTypeCodeAndBetStatOrderByTargetValueDesc(String betTypeCode, String betStat);
    List<BetType> findByBetTypeCodeAndBetStat(String betTypeCode, String betStat);
    BetType findByBetTypeCodeAndTeamAndTargetValue(String betTypeCode, Integer team, Double targetValue);
}
