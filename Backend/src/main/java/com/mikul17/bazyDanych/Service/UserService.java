package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Repository.UserRepository;
import com.mikul17.bazyDanych.Response.UserResponse;
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
            return userRepository.findById(userId).orElseThrow(()
                    -> new ServiceException("User with id: " + userId + " does not exist"));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public UserResponse getUserInfoById(Long userId){
        try{
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ServiceException("User not found"));
            return mapUserToUserResponse(user);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public Double getUserBalanceById(Long userId){
        try{
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ServiceException("User not found"));
            return user.getBalance();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public UserResponse changePhoneNumber(String phoneNumber, Long userId){
        try{
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ServiceException("User not found"));
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);
            return mapUserToUserResponse(user);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public UserResponse changeAccountNumber(String acc, Long userId){
        try{
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ServiceException("User not found"));
            user.setAccountNumber(acc);
            userRepository.save(user);
            return mapUserToUserResponse(user);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    private UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .accountNumber(user.getAccountNumber())
                .balance(user.getBalance())
                .address(user.getAddress())
                .build();
    }
}
