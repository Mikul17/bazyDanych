package com.mikul17.bazyDanych.BetServiceTests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.mikul17.bazyDanych.Models.BetTypeCode;
import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Repository.*;
import com.mikul17.bazyDanych.Service.*;
import com.mikul17.bazyDanych.Models.Matches.MatchStats;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BetServiceTest {

    private BetService betService;

    @Mock
    private BetRepository betRepository;
    @Mock
    private MatchStatsRepository matchStatsRepository;
    @Mock
    private BetTypeService betTypeService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private MatchService matchService;

    @Mock
    private TeamService teamService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        betService = new BetService(betRepository, matchService, matchStatsRepository, betTypeService, couponRepository,teamService);
    }

    @Test
    public void updateScoreBetStatus_HomeTeamWin_BetOnHomeTeam() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType =  BetType.builder()
                .betTypeCode("direct")
                .team(0)
                .targetValue(1.0)
                .build();
        when(bet.getBetType()).thenReturn(betType);
        // Home team scored 2, away scored 1
        MatchStats homeStats = MatchStats.builder()
                .goalsScored(2)
                .build();
        MatchStats awayStats = MatchStats.builder()
                .goalsScored(1)
                .build();

        // Act
        betService.updateScoreBetStatus(bet, homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(1);
    }
    @Test
    public void updateGoalsBetStatus_OverGoals_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("over")
                .team(2) // Combined teams
                .targetValue(2.5)
                .build();
        when(bet.getBetType()).thenReturn(betType);
        // Home scored 2, Away scored 2
        MatchStats homeStats = MatchStats.builder().goalsScored(2).build();
        MatchStats awayStats = MatchStats.builder().goalsScored(2).build();

        // Act
        betService.updateGoalsBetStatus(bet, homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(1); // Total goals = 4, bet wins
    }

    @Test
    public void updateOverBetStatus_PossessionOver_BetLoses() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("over")
                .team(0) // Home team
                .targetValue(60.0) // Target possession percentage
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().possession(55).build(); // Home possession 55%
        MatchStats awayStats = mock(MatchStats.class); // Away stats not needed for this test

        // Act
        betService.updateOverBetStatus(bet, "possession", homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(2); // Possession under 60%, bet loses
    }

    @Test
    public void updateDirectBetStatus_PenaltiesOccurred_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("direct")
                .targetValue(1.0) // Bet on occurrence of penalties
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().penalties(1).build();
        MatchStats awayStats = MatchStats.builder().penalties(0).build();

        // Act
        betService.updateDirectBetStatus(bet, "penalties", homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(1); // Penalties occurred, bet wins
    }

    @Test
    public void updateOverUnderBetStatus_ShotsUnder_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("under")
                .team(1) // Away team
                .targetValue(5.0) // Target shots
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = mock(MatchStats.class); // Home stats not needed for this test
        MatchStats awayStats = MatchStats.builder().shots(3).build(); // Away team shots

        // Act
        betService.updateOverUnderBetStatus(bet, "shots", homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(1); // Shots under 5, bet wins
    }

    @Test
    public void updateDirectBetStatus_RedCardsNotOccurred_BetOnNoRedCards() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("direct")
                .targetValue(0.0) // Bet on no red cards
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().redCards(0).build();
        MatchStats awayStats = MatchStats.builder().redCards(0).build();

        // Act
        betService.updateDirectBetStatus(bet, "redCards", homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(1); // No red cards occurred, bet wins
    }

    @Test
    public void updateOverUnderBetStatus_CornersOver_BetLoses() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("over")
                .team(0) // Home team
                .targetValue(5.0) // Target corners
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().corners(4).build(); // Home team corners

        // Act
        betService.updateOverUnderBetStatus(bet, "corners", homeStats, null);

        // Assert
        verify(bet).setBetStatus(2); // Corners under 5, bet loses
    }

    @Test
    public void updateOverBetStatus_YellowCardsOver_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("over")
                .targetValue(3.0) // Target yellow cards
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().yellowCards(2).build();
        MatchStats awayStats = MatchStats.builder().yellowCards(2).build();

        // Act
        betService.updateOverBetStatus(bet, "yellowCards", homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(1); // Total yellow cards = 4, bet wins
    }

    @Test
    public void updateGoalsBetStatus_UnderGoals_BetLoses() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("under")
                .team(2) // Combined teams
                .targetValue(3.5)
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().goalsScored(2).build();
        MatchStats awayStats = MatchStats.builder().goalsScored(2).build();

        // Act
        betService.updateGoalsBetStatus(bet, homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(2); // Total goals = 4, bet loses
    }

    @Test
    public void updateScoreBetStatus_Draw_BetOnDraw() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType = BetType.builder()
                .betTypeCode("direct")
                .targetValue(2.0) // Bet on draw
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().goalsScored(1).build();
        MatchStats awayStats = MatchStats.builder().goalsScored(1).build();

        // Act
        betService.updateScoreBetStatus(bet, homeStats, awayStats);

        // Assert
        verify(bet).setBetStatus(1); // Match is a draw, bet wins
    }
}