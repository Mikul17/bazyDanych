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
    @Column(name = "transaction_id")
    private Long id;
    @Column(length = 30)
    private String transactionType;
    @Column(length = 10)
    private String transactionStatus;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
}
