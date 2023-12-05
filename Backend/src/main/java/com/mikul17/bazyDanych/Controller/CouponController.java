package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Models.Coupons.Coupon;
import com.mikul17.bazyDanych.Repository.CouponRepository;
import com.mikul17.bazyDanych.Request.CouponRequest;
import com.mikul17.bazyDanych.Service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    //GET
    @GetMapping("/all")
    public List<Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @GetMapping("/{couponId}")
    public Coupon getCouponById(@PathVariable Long couponId) throws Exception {
        return couponService.getCouponById(couponId);
    }

    //POST
    @PostMapping("/add")
    public ResponseEntity<?> addCoupon(@RequestBody CouponRequest couponRequest) {
        return couponService.addCoupon(couponRequest);
    }

    //PUT
    @PutMapping("/cashout/{couponId}")
    public ResponseEntity<?> cashoutCoupon(@PathVariable Long couponId) {
        return couponService.cashoutCoupon(couponId);
    }

    @PutMapping("/update/{couponId}")
    public ResponseEntity<?> updateCouponStatus(@PathVariable Long couponId) {
        try{
            couponService.updateCouponStatus(couponId);
            return ResponseEntity.ok().body("Coupon status updated");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating coupon status: " + e.getMessage());
        }
    }

    @PutMapping("/withdraw/{couponId}")
    public ResponseEntity<?> withdrawCoupon(@PathVariable Long couponId) {
        return couponService.withdrawCoupon(couponId);
    }
}
