package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.Coupon;
import com.mikul17.bazyDanych.Repository.CouponRepository;
import com.mikul17.bazyDanych.Request.CouponRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private static final Logger log = LoggerFactory.getLogger(CouponService.class);

    private final CouponRepository couponRepository;

    //C - create
    public ResponseEntity<?> addCoupon (CouponRequest couponRequest) {
        try{
            Coupon coupon = Coupon.builder()
                    .creationDate(new Timestamp(System.currentTimeMillis()))
                    .couponStatus("active")
                    .stake(couponRequest.getStake())
                    .bets(couponRequest.getBets())
                    .totalOdds(couponRequest.getBets().stream().mapToDouble(Bet::getOdds).sum())
                    .build();
            coupon.setPossibleWin(coupon.getTotalOdds() * coupon.getStake());
            couponRepository.save(coupon);
            return ResponseEntity.ok().body("Coupon created successfully with " + coupon.getBets().size() + "bets in it");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error with creating coupon");
        }
    }
    //R - read
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }
    public Coupon getCouponById(Long couponId) throws Exception {
        return couponRepository.findById(couponId).orElseThrow(() -> new Exception("Coupon not found"));
    }

    //U - update - not needed
    public ResponseEntity<?> cashoutCoupon (Long couponId) {
        try{
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new Exception("Coupon not found"));
            double amount = 0.0;
            for(Bet bet : coupon.getBets()) {
                if (bet.getBetStatus() == 0) {
                    return ResponseEntity.ok().body("Coupon has not been checked yet");
                }else if(bet.getBetStatus() == 2){
                    return ResponseEntity.badRequest().body("Coupon is lost, cannot cash out");
                }else {
                    amount += bet.getOdds() * coupon.getStake();
                }
            }
            coupon.setCouponStatus("cashed_out");
            coupon.setPossibleWin(amount);
            couponRepository.save(coupon);
            return ResponseEntity.ok().body("Coupon cashed out successfully. You've won " + amount + "PLN");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error with cashing out coupon");
        }
    }
    public void updateCouponStatus (Long couponId){
        try{
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new Exception("Coupon not found"));
            int counter = 0;
            for(Bet bet : coupon.getBets()) {
                if (bet.getBetStatus() == 0) {
                    coupon.setCouponStatus("active");
                }else if(bet.getBetStatus() == 2){
                    coupon.setCouponStatus("lost");
                }else{
                    counter++;
                }
            }
            if(counter == coupon.getBets().size()){
                coupon.setCouponStatus("won");
            }
            couponRepository.save(coupon);
            log.info("Coupon status updated to " + coupon.getCouponStatus());
        }catch (Exception e){
           log.error("Error with updating coupon status");
        }
    }

    //D - delete
    public ResponseEntity<?> withdrawCoupon (Long couponId) {
        try{
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new Exception("Coupon not found"));
            coupon.setCouponStatus("won");
            couponRepository.save(coupon);
            return ResponseEntity.ok().body("Coupon withdrawn successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error with withdrawing coupon: "+ e.getMessage());
        }
    }
}
