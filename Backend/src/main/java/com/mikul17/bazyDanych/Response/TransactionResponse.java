package com.mikul17.bazyDanych.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private String transactionType;
    private String transactionStatus;
    private Double amount;
    private Long userId;
}
