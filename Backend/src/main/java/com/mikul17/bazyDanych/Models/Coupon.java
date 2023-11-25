package com.mikul17.bazyDanych.Models;

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
    @Column(name = "couponId")
    private Long id;
    private Timestamp creationDate;
    private Double totalOdds;
    private String couponStatus;
    private Double possibleWin;

    @ManyToOne
    @JoinColumn(name = "userId")
    @Column(name = "userId")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "coupon_bet",
            joinColumns = @JoinColumn(name = "couponId"),
            inverseJoinColumns = @JoinColumn(name = "betId"))
    private List<Bet> bet;
}
