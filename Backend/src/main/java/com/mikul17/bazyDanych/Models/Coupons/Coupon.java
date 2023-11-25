package com.mikul17.bazyDanych.Models.Coupons;

import com.mikul17.bazyDanych.Models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Coupon {
    @Id
    @Column(name = "coupon_id")
    private Long id;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creationDate;
    private Double totalOdds;
    private String couponStatus;
    private Double possibleWin;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "coupon_bet",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "bet_id"))
    private List<Bet> bet;
}
