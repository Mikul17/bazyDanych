package com.mikul17.bazyDanych.Security.Service;

import com.mikul17.bazyDanych.Models.Role;
import com.mikul17.bazyDanych.Models.Address;
import com.mikul17.bazyDanych.Repository.UserRepository;
import com.mikul17.bazyDanych.Request.AuthenticationRequest;
import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Request.RegisterRequest;
import com.mikul17.bazyDanych.Response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public static String hashSSN(String ssn) throws NoSuchAlgorithmException {
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

    public AuthenticationResponse register (RegisterRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                return AuthenticationResponse.builder()
                        .message("Email already exists")
                        .build();
            }
            if (userRepository.existsBySSN(hashSSN(request.getSSN()))) {
                return AuthenticationResponse.builder()
                        .message("SSN already exists")
                        .build();
            }

            if (decodeAgeFromSSN(request.getSSN()) < 18) {
                return AuthenticationResponse.builder()
                        .message("Error: You have to be at least 18 years old")
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


            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(encoder.encode(request.getPassword()))
                    .phoneNumber(request.getPhoneNumber())
                    .birthDate(parseBirthdateFromSSN(request.getSSN()))
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
        }catch (Exception e){
            return AuthenticationResponse.builder()
                    .message(e.getMessage())
                    .build();
        }
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

    private Integer decodeAgeFromSSN(String ssn){
        String extractedBirthdate = ssn.substring(0,6);
        LocalDate birthDate = parseBirthdateFromSSN(extractedBirthdate);
        LocalDate now = LocalDate.now();

        return Period.between(birthDate,now).getYears();
    }

    private LocalDate parseBirthdateFromSSN(String ssnDate){
        int year = Integer.parseInt(ssnDate.substring(0,2));
        int month = Integer.parseInt(ssnDate.substring(2,4));
        int day = Integer.parseInt(ssnDate.substring(4,6));


        if (month > 20 && month < 33) {
            year += 2000;
            month -= 20;
        } else if (month > 0 && month < 13) {
            year += 1900;
        } else {
            throw new IllegalArgumentException("Invalid birth month in PESEL number");
        }

        if(!isValidDate(day,month,year)){
            throw new IllegalArgumentException("Invalid birthdate in PESEL");
        }
        return LocalDate.of(year,month,day);
    }

    private  boolean isValidDate(int day, int month, int year) {
        try {
            if (year < LocalDate.MIN.getYear() || year > LocalDate.MAX.getYear()) {
                return false;
            }

            YearMonth yearMonth = YearMonth.of(year, month);

            return day >= 1 && day <= yearMonth.lengthOfMonth();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid birthdate in PESEL");
        }
    }
}
