package com.mikul17.bazyDanych.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "First name is required")
    @Size(max = 30, message = "First name cannot be longer than 30 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 30, message = "Last name cannot be longer than 30 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email cannot be longer than 50 characters")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(max = 50, message = "Password cannot be longer than 50 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Size(min = 2, max = 15, message = "Phone number must be between 2 and 15 characters")
    private String phoneNumber;

    private LocalDate birthDate;

    @Size(min = 26, max = 26, message = "Account number must be 26 characters long")
    private String accountNumber;

    @Size(min = 11, max = 11, message = "SSN must be 11 characters long")
    @NotBlank(message = "SSN is required")
    private String SSN;

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
