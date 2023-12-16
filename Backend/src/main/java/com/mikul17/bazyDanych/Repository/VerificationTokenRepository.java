package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.TokenType;
import com.mikul17.bazyDanych.Models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    public VerificationToken findVerificationTokenByToken(String token);
}
