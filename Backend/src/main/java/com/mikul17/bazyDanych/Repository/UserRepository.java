package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Enums.Role;
import com.mikul17.bazyDanych.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySSN(String SSN);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByAccountNumber(String accountNumber);

    List<User> findByRole(Role role);
    boolean existsByEmail (String email);

    boolean existsBySSN (String ssn);

    Integer countByAddress_Id(Long address_id);
}
