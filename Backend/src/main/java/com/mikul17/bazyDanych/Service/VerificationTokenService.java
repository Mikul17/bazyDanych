package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.TokenType;
import com.mikul17.bazyDanych.Models.VerificationToken;
import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository repository;

    private final Logger logger = LoggerFactory.getLogger(VerificationTokenService.class);

    public String  generateVerificationToken(User user){
        try {
            VerificationToken token = VerificationToken.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user)
                    .tokenType(TokenType.EMAIL_VERIFICATION)
                    .expiryDate(calculateExpiryDate(3600))
                    .build();
            repository.save(token);
            return token.getToken();
        }catch (Exception e){
            throw new ServiceException("Error while generating token: "+e.getMessage());
        }
    }

    public String generateRemindPasswordToken(User user){
        try {
            VerificationToken token = VerificationToken.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user)
                    .tokenType(TokenType.PASSWORD_RESET)
                    .expiryDate(calculateExpiryDate(30))
                    .build();
            repository.save(token);
            return token.getToken();
        }catch (Exception e){
            throw new ServiceException("Error while generating token: "+e.getMessage());
        }
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerificationToken findByToken (String token) {
        try{
            VerificationToken t = repository.findVerificationTokenByToken(token);
            if (t.getTokenType()!=TokenType.EMAIL_VERIFICATION){
                throw new ServiceException("Wrong token type");
            }
            return t;
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
