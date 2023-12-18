package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByCountryAndCityAndStreetAndStreetNumberAndHouseNumberAndZipCode(
            String country, String city, String street, String streetNumber, String houseNumber, String zipCode);

    Optional<Address> findByCountryAndCityAndStreetAndStreetNumberAndHouseNumberAndZipCode (String country, String city, String street, String streetNumber, String houseNumber, String zipCode);
}
