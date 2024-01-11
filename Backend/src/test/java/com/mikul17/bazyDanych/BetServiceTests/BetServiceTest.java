package com.mikul17.bazyDanych.BetServiceTests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.mikul17.bazyDanych.Models.BetStat;
import com.mikul17.bazyDanych.Models.BetTypeCode;
import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Repository.*;
import com.mikul17.bazyDanych.Service.*;
import com.mikul17.bazyDanych.Models.Matches.MatchStats;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public void updateScoreBetStatus_HomeTeamWin_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        BetType betType =  BetType.builder()
                .betTypeCode(BetTypeCode.DIRECT.getCode())
                .team(0)
                .targetValue(0.0)
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
        //catch betStatus value
        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(1), betStatusCaptor.getValue());
    }
    @Test
    public void updateScoreBetStatus_Draw_BetLoses() {
        // Arrange
        Bet bet = mock(Bet.class);
        //There will be draw in match
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.DIRECT.getCode())
                .targetValue(2.0)
                .team(2)
                .build();

        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().goalsScored(2).build();
        MatchStats awayStats = MatchStats.builder().goalsScored(1).build();

        // Act
        betService.updateScoreBetStatus(bet, homeStats, awayStats);

        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(2), betStatusCaptor.getValue());
    }

    @Test
    public void updateScoreBetStatus_AwayTeamWinOrDraw_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        //Away team will win or draw
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.DIRECT.getCode())
                .targetValue(1.0)
                .team(1)
                .build();

        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().goalsScored(2).build();
        MatchStats awayStats = MatchStats.builder().goalsScored(2).build();

        // Act
        betService.updateScoreBetStatus(bet, homeStats, awayStats);

        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(1), betStatusCaptor.getValue());
    }
    @Test
    public void updateGoalsBetStatus_BothOverGoals_BetWins() {
        Bet bet = mock(Bet.class);
        // Over 2.5 goals scored by both teams
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.OVER.getCode())
                .betStat(BetStat.GOALS.getStat())
                .team(2)
                .targetValue(2.5)
                .build();

        when(bet.getBetType()).thenReturn(betType);

        // Home scored 2, Away scored 2
        MatchStats homeStats = MatchStats.builder().goalsScored(2).build();
        MatchStats awayStats = MatchStats.builder().goalsScored(2).build();

        betService.updateGoalsBetStatus(bet,homeStats,awayStats);

        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(1), betStatusCaptor.getValue());
    }
    @Test
    public void updateGoalsBetStatus_BothTeamsUnderGoals_BetLoses() {
        // Arrange
        Bet bet = mock(Bet.class);
        //Both teams score under 3.5 goals
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.UNDER.getCode())
                .team(2)
                .targetValue(3.5)
                .betStat(BetStat.GOALS.getStat())
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().goalsScored(3).build();
        MatchStats awayStats = MatchStats.builder().goalsScored(2).build();

        // Act
        betService.updateGoalsBetStatus(bet,homeStats,awayStats);

        // Assert
        //catch betStatus value
        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(2), betStatusCaptor.getValue());
    }
    @Test
    public void updateOverBetStatus_HomeTeamPossessionOver_BetLoses() {
        // Arrange
        Bet bet = mock(Bet.class);
        //Home team will get over 60% possession
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.OVER.getCode())
                .team(0)
                .targetValue(60.0)
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().possession(55).build(); // Home possession 55%
        MatchStats awayStats = mock(MatchStats.class); // Away stats not needed for this test

        // Act
        betService.updateOverBetStatus(bet, BetStat.POSSESSION.getStat(), homeStats, awayStats);

        // Assert
        //catch betStatus value
        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(2), betStatusCaptor.getValue());
    }
    @Test
    public void updateDirectBetStatus_PenaltiesOccurred_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        //Check if penalty occurred in match
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.DIRECT.getCode())
                .targetValue(1.0)
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().penalties(1).build();
        MatchStats awayStats = MatchStats.builder().penalties(0).build();

        // Act
        betService.updateDirectBetStatus(bet, BetStat.PENALTIES.getStat(), homeStats, awayStats);

        // Assert
        //catch betStatus value
        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(1), betStatusCaptor.getValue());
    }
    @Test
    public void updateOverUnderBetStatus_ShotsUnder_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        //Away team will have under 5.5 shots
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.UNDER.getCode())
                .team(1)
                .targetValue(5.5)
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = mock(MatchStats.class);
        MatchStats awayStats = MatchStats.builder().shots(3).build();

        // Act
        betService.updateOverUnderBetStatus(bet, BetStat.SHOTS.getStat(), homeStats, awayStats);

        // Assert
        //catch betStatus value
        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(1), betStatusCaptor.getValue());
    }

    @Test
    public void updateDirectBetStatus_RedCardsNotOccurred_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        //No red card in match
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.DIRECT.getCode())
                .targetValue(0.0)
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().redCards(0).build();
        MatchStats awayStats = MatchStats.builder().redCards(0).build();

        // Act
        betService.updateDirectBetStatus(bet, BetStat.RED_CARDS.getStat(), homeStats, awayStats);

        // Assert
        //catch betStatus value
        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(1), betStatusCaptor.getValue());
    }

    @Test
    public void updateOverUnderBetStatus_CornersOver_BetLoses() {
        // Arrange
        Bet bet = mock(Bet.class);
        //Home team over 5.5 corners
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.OVER.getCode())
                .team(0) // Home team
                .targetValue(5.5) // Target corners
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().corners(4).build(); // Home team corners

        // Act
        betService.updateOverUnderBetStatus(bet, BetStat.CORNERS.getStat(), homeStats, null);

        // Assert
        //catch betStatus value
        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(2), betStatusCaptor.getValue());
    }

    @Test
    public void updateOverBetStatus_YellowCardsOver_BetWins() {
        // Arrange
        Bet bet = mock(Bet.class);
        //Over 3.5 yellow cards in match
        BetType betType = BetType.builder()
                .betTypeCode(BetTypeCode.OVER.getCode())
                .team(2)
                .targetValue(3.5) // Target yellow cards
                .build();
        when(bet.getBetType()).thenReturn(betType);
        MatchStats homeStats = MatchStats.builder().yellowCards(2).build();
        MatchStats awayStats = MatchStats.builder().yellowCards(2).build();

        // Act
        betService.updateOverBetStatus(bet, BetStat.YELLOW_CARDS.getStat(), homeStats, awayStats);

        // Assert
        // Assert
        //catch betStatus value
        ArgumentCaptor<Integer> betStatusCaptor = ArgumentCaptor.forClass(Integer.class);
        //verify if status is being changed inside function
        verify(bet).setBetStatus(betStatusCaptor.capture());
        //check if status is correctly assigned
        Assert.assertEquals(Integer.valueOf(1), betStatusCaptor.getValue());
    }




}