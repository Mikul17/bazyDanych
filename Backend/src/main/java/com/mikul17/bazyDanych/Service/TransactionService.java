package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Transaction;
import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Repository.TransactionRepository;
import com.mikul17.bazyDanych.Repository.UserRepository;
import com.mikul17.bazyDanych.Request.TransactionRequest;
import com.mikul17.bazyDanych.Response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final Logger logger  = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public TransactionResponse performTransaction(TransactionRequest transactionRequest){
        try{
            User user  = userService.getUserById(transactionRequest.getUserId());
            Transaction trans = Transaction.builder()
                    .transactionType(transactionRequest.getType())
                    .transactionStatus("PENDING")
                    .amount(transactionRequest.getAmount())
                    .user(user)
                    .date(Timestamp.valueOf(LocalDateTime.now()))
                    .build();

            if(Objects.equals(trans.getTransactionType(), "DEPOSIT")
                    ||  Objects.equals(trans.getTransactionType(), "PAYOUT")) {
                user.setBalance(user.getBalance() + trans.getAmount());
                trans.setTransactionStatus("SUCCESS");
            }

            if(Objects.equals(trans.getTransactionType(),"WITHDRAW")){
                if(user.getBalance() < trans.getAmount()){
                    trans.setTransactionStatus("DECLINED");
                }else{
                    user.setBalance(user.getBalance()-trans.getAmount());
                    trans.setTransactionStatus("SUCCESS");
                }
            }
            userRepository.save(user);
            transactionRepository.save(trans);
            return mapTransactionToTransactionResponse(trans);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<TransactionResponse> getAllTransactions() {
        try {
            return transactionRepository.findAll().stream().map(this::mapTransactionToTransactionResponse)
                    .toList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<TransactionResponse> getAllTransactionsByUserId(Long userId) {
        try {
            User user = userService.getUserById(userId);
            return transactionRepository.findAllByUserAndTransactionStatusOrderByDateDesc(user, "SUCCESS")
                    .stream().map(this::mapTransactionToTransactionResponse)
                    .toList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<TransactionResponse> getTransactionByUserAndType(Long userId,String type) {
        try {
            User user = userService.getUserById(userId);
            return transactionRepository.findAllByUserAndTransactionStatusAndTransactionType(user, "SUCCESS", type)
                    .stream().map(this::mapTransactionToTransactionResponse)
                    .toList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    private TransactionResponse mapTransactionToTransactionResponse(Transaction transaction){
        return new TransactionResponse(transaction.getId(), transaction.getTransactionType(),
                transaction.getTransactionStatus(), transaction.getAmount(),
                transaction.getUser().getId(),transaction.getDate());
    }

}
