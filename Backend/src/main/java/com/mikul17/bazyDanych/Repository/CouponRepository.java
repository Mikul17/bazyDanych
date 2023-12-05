package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Coupons.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
