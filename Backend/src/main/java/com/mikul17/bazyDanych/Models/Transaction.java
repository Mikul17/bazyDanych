package com.mikul17.bazyDanych.Models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity(name = "transaction")
public class Transaction {
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String transactionType;
    @Column(length = 10)
    private String transactionStatus;
    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double amount;

    @Column(name = "transaction_date")
    private Timestamp date;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
}
