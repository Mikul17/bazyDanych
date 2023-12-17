package com.mikul17.bazyDanych.BetTypeTests;

import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Repository.BetTypeRepository;
import com.mikul17.bazyDanych.Service.BetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BetTypeServiceTest {

    private BetTypeService betTypeService;
    private BetTypeRepository betTypeRepository;

    @BeforeEach
    public void setUp() {
        betTypeService = new BetTypeService(betTypeRepository);
    }

    // Adjusted test cases according to the new decodeBetFromBetType method

    // Test Case: Home team (0), Bet Type "direct", Target Value 1.0 (win or draw)
    @Test
    public void testDecodeBetForHomeTeamDirectWinOrDraw() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(0); // Home team
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(1.0);
        when(betType.getBetStat()).thenReturn("score");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("Home team will win or draw the match", result);
    }

    // Test Case: Away team (1), Bet Type "direct", Target Value 0.0 (win)
    @Test
    public void testDecodeBetForAwayTeamDirectWin() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(1); // Away team
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(0.0);
        when(betType.getBetStat()).thenReturn("score");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("Away team will win the match", result);
    }

    // Test Case: Draw (2), Bet Type "direct", Target Value 2.0
    @Test
    public void testDecodeBetForDraw() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(2); // Draw scenario
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(2.0);
        when(betType.getBetStat()).thenReturn("score");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("This match will end in a draw", result);
    }
    @Test
    public void testDecodeBetForPenalties() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(2);
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(1.0);
        when(betType.getBetStat()).thenReturn("penalties");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("There will be penalty in this match", result);
    }

    // Test Case: Draw (2), Bet Type "direct", Target Value 0.0, Bet Stat "redCards" (no red cards will occur)
    @Test
    public void testDecodeBetForNoRedCards() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(2);
        when(betType.getBetTypeCode()).thenReturn("direct");
        when(betType.getTargetValue()).thenReturn(0.0);
        when(betType.getBetStat()).thenReturn("redCards");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("There will not be red card in this match", result);
    }

    // Test Case: Home team, Bet Type "over", Target Value 2.5, Bet Stat "goals"
    @Test
    public void testDecodeBetForOverGoals() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(0);
        when(betType.getBetTypeCode()).thenReturn("over");
        when(betType.getTargetValue()).thenReturn(2.5);
        when(betType.getBetStat()).thenReturn("goals");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("Home team will get over 2.5 goals in this match", result);
    }

    // Test Case: Away team, Bet Type "under", Target Value 3.5, Bet Stat "shots"
    @Test
    public void testDecodeBetForUnderShots() {
        BetType betType = mock(BetType.class);
        when(betType.getTeam()).thenReturn(1);
        when(betType.getBetTypeCode()).thenReturn("under");
        when(betType.getTargetValue()).thenReturn(3.5);
        when(betType.getBetStat()).thenReturn("shots");

        String result = betTypeService.decodeBetFromBetType(betType);
        assertEquals("Away team will get under 3.5 shots in this match", result);
    }
}
