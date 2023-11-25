package com.mikul17.bazyDanych.Models.Coupons;

import com.mikul17.bazyDanych.Models.Matches.Match;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bet {
    @Id
    @Column(name = "bet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = Match.class)
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double odds;

    @ManyToOne
    @JoinColumn(name = "bet_type_id")
    private BetType betType;

}
