package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.TokenType;
import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Models.VerificationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

     VerificationToken findVerificationTokenByToken(String token);

     boolean existsByUser (User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.expiryDate < CURRENT_TIMESTAMP")
    void deleteAllExpiredTokens();

}
