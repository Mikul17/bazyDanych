package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public void checkIfUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ServiceException("User with id: " + userId + " does not exist");
        }
    }

    public User getUserById(Long userId) {
        try{
            return userRepository.findById(userId).orElseThrow(() -> new ServiceException("User with id: " + userId + " does not exist"));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
