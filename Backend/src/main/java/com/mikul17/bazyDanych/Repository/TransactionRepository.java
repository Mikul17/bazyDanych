package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Transaction;
import com.mikul17.bazyDanych.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserAndTransactionStatusAndTransactionType (User user, String transactionStatus, String transactionType);
    List<Transaction> findAllByUserAndTransactionStatus (User user, String transactionStatus);

}
