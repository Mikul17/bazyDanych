package com.mikul17.bazyDanych.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    private String type;
    private Long userId;
    private Double amount;
}

