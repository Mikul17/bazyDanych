package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

}