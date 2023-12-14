package com.mikul17.bazyDanych.BetTests;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Models.Coupons.Coupon;
import com.mikul17.bazyDanych.Repository.BetRepository;
import com.mikul17.bazyDanych.Repository.BetTypeRepository;
import com.mikul17.bazyDanych.Repository.CouponRepository;
import com.mikul17.bazyDanych.Service.BetService;
import com.mikul17.bazyDanych.Service.BetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BetTypeServiceTest {

    private BetTypeService betTypeService;
    private BetTypeRepository betTypeRepository;
    @BeforeEach
    public void setUp() {
        betTypeService = new BetTypeService(betTypeRepository);
    }

    // Test Case: Team is null, Bet Type "over"
    @Test
    public void testDecodeBetWhenTeamIsNullAndTypeIsOver() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(null);
        when(betType.getBetTypeCode()).thenReturn("over");
        when(betType.getTargetValue()).thenReturn(2.5);
        when(betType.getBetStat()).thenReturn("goals");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("There will be over 2.5 goals in this match", result);
    }

    // Test Case: Team is null, Bet Type "under"
    @Test
    public void testDecodeBetWhenTeamIsNullAndTypeIsUnder() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(null);
        when(betType.getBetTypeCode()).thenReturn("under");
        when(betType.getTargetValue()).thenReturn(1.5);
        when(betType.getBetStat()).thenReturn("cards");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("There will be under 1.5 cards in this match", result);
    }

    // Test Case: Home team, Bet Type "direct", Target Value not 1.1
    @Test
    public void testDecodeBetForHomeTeamDirectWin() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(false);
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(2.0);

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("Home team will win the match", result);
    }

    // Test Case: Home team, Bet Type "direct", Target Value 1.1
    @Test
    public void testDecodeBetForHomeTeamDirectWinOrDraw() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(false);
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(1.1);

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("Home team will win or draw the match", result);
    }

    // Test Case: Away team, Bet Type "direct", Target Value not 1.1
    @Test
    public void testDecodeBetForAwayTeamDirectWin() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(true);
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(2.0);

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("Away team will win the match", result);
    }

    // Test Case: Away team, Bet Type "direct", Target Value 1.1
    @Test
    public void testDecodeBetForAwayTeamDirectWinOrDraw() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(true);
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(1.1);

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("Away team will win or draw the match", result);
    }
}
