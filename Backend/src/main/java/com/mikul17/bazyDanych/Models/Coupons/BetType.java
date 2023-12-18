package com.mikul17.bazyDanych.Models.Coupons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "bet_type")
public class BetType {
    @Id
    @Column(name = "bet_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String betStat;
    private String betTypeCode;
    private Integer team;
    @Column (columnDefinition = "DECIMAL(2,1)")
    private Double targetValue;
    @JsonIgnore
    @OneToMany(mappedBy = "betType",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Bet> bets;
}
