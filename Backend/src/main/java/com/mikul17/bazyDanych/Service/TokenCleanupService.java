package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final Logger logger = LoggerFactory.getLogger(TokenCleanupService.class);
    private final VerificationTokenRepository tokenRepository;
    @Scheduled(fixedRate = 1000*60*30) //every 30 min - when password token gets expired
    private void cleanupExpiredTokens(){
        tokenRepository.deleteAllExpiredTokens();
        logger.info("Deleting all expired tokens");
    }
}
