package com.mikul17.bazyDanych.Models;

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
    @Column(name = "betId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long matchId;
    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double odds;

    @ManyToOne
    @JoinColumn(name = "betTypeId")
    private BetType betType;

}
