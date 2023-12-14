package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.Coupon;
import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByBetsId(Long betId);

    List<Coupon> findAllByUser (User user);

    List<Coupon> findAllByBets_Match (Match match);

    List<Coupon> findByUserAndCouponStatus (User user, String won);
}