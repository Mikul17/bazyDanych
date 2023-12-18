package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Address;
import com.mikul17.bazyDanych.Repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    Address findOrCreateAddress (Address request) {
        return addressRepository
                .findByCountryAndCityAndStreetAndStreetNumberAndHouseNumberAndZipCode(
                        request.getCountry(), request.getCity(), request.getStreet(),
                        request.getStreetNumber(), request.getHouseNumber(), request.getZipCode())
                .orElseGet(() -> addressRepository.save(request));
    }

    public Address addNewAddress(Address address) {
        if (existsByAddress(address)) {
            throw new ServiceException("Address already exists");
        }
        return addressRepository.save(address);
    }

    void updateAddressDetails (Address currentAddress, Address newDetails) {
        currentAddress.setCountry(newDetails.getCountry());
        currentAddress.setCity(newDetails.getCity());
        currentAddress.setStreet(newDetails.getStreet());
        currentAddress.setStreetNumber(newDetails.getStreetNumber());
        currentAddress.setHouseNumber(newDetails.getHouseNumber());
        currentAddress.setZipCode(newDetails.getZipCode());
        addressRepository.save(currentAddress);
    }
    public boolean existsByAddress(Address address) {
        try {

            if (address == null) {
                throw new ServiceException("Address cannot be empty");
            }
            return addressRepository.existsByCountryAndCityAndStreetAndStreetNumberAndHouseNumberAndZipCode(
                    address.getCountry(), address.getCity(), address.getStreet(),
                    address.getStreetNumber(), address.getHouseNumber(), address.getZipCode());
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public Address getById (Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(
                () -> new ServiceException("Address with given id: "+ addressId+" doesnt exist"));
    }
}
