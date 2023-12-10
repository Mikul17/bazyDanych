package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

}
