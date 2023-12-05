package com.mikul17.bazyDanych.Models.Coupons;

import com.mikul17.bazyDanych.Models.Matches.Match;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
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

    // 0 - not checked, 1 - won, 2 - lost
    @Column(columnDefinition = "INT(2) DEFAULT 0")
    private Integer betStatus;

    @ManyToOne
    @JoinColumn(name = "bet_type_id")
    private BetType betType;
}
