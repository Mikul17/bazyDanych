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
@Entity(name = "transaction")
public class Transaction {
    @Id
    @Column(name = "transactionId")
    private Long id;
    private String transactionType;
    private String transactionStatus;

    @ManyToOne(targetEntity = User.class)
    @Column(name = "userId")
    @JoinColumn(name = "userId")
    private User user;
}
