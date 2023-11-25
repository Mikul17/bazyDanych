package com.mikul17.bazyDanych.Models;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "user")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long Id;
    @Column(nullable = false, columnDefinition = "varchar(30)")
    private String firstName;
    @Column(nullable = false, columnDefinition = "varchar(30)")
    private String lastName;
    @Column(nullable = false, columnDefinition = "varchar(45)")
    private String email;
    @Column(nullable = false, columnDefinition = "varchar(25)")
    private String password;
    @Column(nullable = false, columnDefinition = "varchar(15)")
    private String phoneNumber;
    private LocalDate birthDate;
    @Column(columnDefinition = "varchar(26)")
    private String accountNumber;
    @Column(nullable = false, columnDefinition = "varchar(11)")
    private String SSN;
    @Column(nullable = false, columnDefinition = "varchar(5)")
    private String role;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
    @Column(columnDefinition = "DECIMAL(19,2)")
    private Double balance;

    @ManyToOne(targetEntity = Address.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address Address;

    //User now holds list of transaction (no need to use sql query to get them, and
    //they are not stored in database as separate column in user table)
    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
