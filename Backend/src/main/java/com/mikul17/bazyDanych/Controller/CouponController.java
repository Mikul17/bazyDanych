package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.Coupon;
import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Repository.CouponRepository;
import com.mikul17.bazyDanych.Request.CouponRequest;
import com.mikul17.bazyDanych.Service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCoupons() {
       try{
           return ResponseEntity.ok(couponService.getAllCoupons());
         } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<?> getAllCouponsByUserId(@PathVariable Long userId) {
        try{
            return ResponseEntity.ok(couponService.getAllCouponsByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<?> getCouponById(@PathVariable Long couponId) {
        try{
            return ResponseEntity.ok(couponService.getCouponById(couponId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCoupon(@RequestBody CouponRequest couponRequest) {
        try{
            return ResponseEntity.ok(couponService.addCoupon(couponRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{couponId}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long couponId) {
        try{
            couponService.deleteCoupon(couponId);
            return ResponseEntity.ok("Coupon with id: " + couponId + " deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/won/{userId}")
    public ResponseEntity<?> getWonCouponsByUserId(@PathVariable Long userId) {
        try{
            return ResponseEntity.ok(couponService.getWonCouponsByUserId(userId, "WON"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/lost/{userId}")
    public ResponseEntity<?> getLostCouponsByUserId(@PathVariable Long userId) {
        try{
            return ResponseEntity.ok(couponService.getWonCouponsByUserId(userId, "LOST"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}