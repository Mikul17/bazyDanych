package com.mikul17.bazyDanych.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "address")
public class Address {
    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2)
    private String country;
    @Column(length = 50)
    private String city;
    @Column(length = 50)
    private String street;
    @Column(length = 5)
    private String streetNumber;
    @Column(length = 5)
    private String houseNumber;
    @Column(length = 6)
    private String zipCode;

}
