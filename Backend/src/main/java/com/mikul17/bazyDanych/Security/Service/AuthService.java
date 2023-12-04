package com.mikul17.bazyDanych.Security.Service;

import com.mikul17.bazyDanych.Models.Role;
import com.mikul17.bazyDanych.Models.Address;
import com.mikul17.bazyDanych.Repository.UserRepository;
import com.mikul17.bazyDanych.Request.AuthenticationRequest;
import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Request.RegisterRequest;
import com.mikul17.bazyDanych.Response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public static String hashSSN(String ssn) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(ssn.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public AuthenticationResponse register (RegisterRequest request) throws NoSuchAlgorithmException {
        if(userRepository.existsByEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .message("Email already exists")
                    .build();
        }
        if(userRepository.existsBySSN(hashSSN(request.getSSN()))) {
            return AuthenticationResponse.builder()
                    .message("SSN already exists")
                    .build();
        }

        var address = Address.builder()
                .country(request.getCountry())
                .city(request.getCity())
                .street(request.getStreet())
                .streetNumber(request.getStreetNumber())
                .houseNumber(request.getHouseNumber())
                .zipCode(request.getZipCode())
                .build();

        System.out.println(request.getSSN());
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .accountNumber(request.getAccountNumber())
                .SSN(hashSSN(request.getSSN()))
                .role(Role.ROLE_USER)
                .createdAt(new Timestamp(new Date().getTime()))
                .balance(0.0)
                .Address(address)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .message("User successfully registered")
                .token(jwtToken)
                .email(user.getEmail())
                .build();
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .message("User successfully logged in")
                .email(user.getEmail())
                .token(jwtToken)
                .build();
    }
}
