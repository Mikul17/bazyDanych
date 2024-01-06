package com.mikul17.bazyDanych.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithBanStatusResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isBanned;
}
