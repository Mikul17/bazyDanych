package com.mikul17.bazyDanych.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BetTypeResponse {

    private String responseCode;
    private String message;
    private String decodedBetType;
}
