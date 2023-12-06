package com.mikul17.bazyDanych.Repository;

import com.mikul17.bazyDanych.Models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>{
}
