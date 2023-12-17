package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Matches.MatchStats;
import com.mikul17.bazyDanych.Repository.*;
import com.mikul17.bazyDanych.Request.BetRequest;
import com.mikul17.bazyDanych.Response.BetResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;
    private final BetTypeRepository betTypeRepository;
    private final CouponRepository couponRepository;
    private final MatchRepository matchRepository;
    private final MatchStatsRepository matchStatsRepository;

    public BetResponse createBet(BetRequest betRequest) {
        try {
            Match match = matchRepository.findById(betRequest.getMatchId())
                    .orElseThrow(() -> new ServiceException("Match not found"));
            BetType betType = betTypeRepository.findById(betRequest.getBetTypeId())
                    .orElseThrow(() -> new ServiceException("Bet type not found"));
            Bet bet = Bet.builder()
                    .match(match)
                    .odds(betRequest.getOdds())
                    .betType(betType)
                    .betStatus(0)
                    .build();
            betRepository.save(bet);

            return mapBetToBetResponse(bet);
        }catch (Exception e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public List<BetResponse> getAllBets() {
        try {
            return betRepository.findAll().stream()
                    .map(this::mapBetToBetResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public BetResponse getBetById (Long id) {
        try{
            Bet bet = betRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Bet not found"));
            return mapBetToBetResponse(bet);
        }catch (Exception e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public List<Bet> getBetsByMatchId(Long matchId) {
        try{
            Match match = matchRepository.findById(matchId).orElseThrow(()
                    -> new ServiceException("Match not found"));
            return betRepository.findByMatch(match);
        }catch (Exception e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public BetResponse updateBet (Long id, BetRequest betRequest) {
        try {
            Bet bet = betRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Bet not found"));
            Match match = matchRepository.findById(betRequest.getMatchId())
                    .orElseThrow(() -> new ServiceException("Match not found"));
            BetType betType = betTypeRepository.findById(betRequest.getBetTypeId())
                    .orElseThrow(() -> new ServiceException("Bet type not found"));
            bet.setMatch(match);
            bet.setOdds(betRequest.getOdds());
            bet.setBetType(betType);

            betRepository.save(bet);
            return mapBetToBetResponse(bet);
        }catch (Exception e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public BetResponse changeBetStatus(Long id, Integer status){
        try{
            Bet bet = betRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Bet not found"));
            if(status < 0 || status > 2)
            {throw new ServiceException("Wrong status value");}
           bet.setBetStatus(status);
            return mapBetToBetResponse(bet);
        }catch (Exception e){
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

   public void updateOdds(Bet bet){
        try{
            int amountOfBets = couponRepository.findByBetsId(bet.getId()).size();

            List<Bet> relatedBets = findRelatedBets(bet.getBetType(), bet.getMatch());
            bet.setOdds(calculateNewOdds(bet.getOdds(), amountOfBets,false));

            for(Bet relatedBet : relatedBets){
                relatedBet.setOdds(calculateNewOdds(relatedBet.getOdds(),amountOfBets, true));
            }

            betRepository.save(bet);
        }catch (Exception e){
            throw new ServiceException("Error: " + e.getMessage());
        }
   }

   //Tweak numbers if needed
   private Double calculateNewOdds(Double oldOdds, int betsPlaced, boolean requiresAddition){
       double adjustmentFactor = Math.log1p(betsPlaced) * 0.01;
       return requiresAddition ? oldOdds + adjustmentFactor * oldOdds : oldOdds - adjustmentFactor * oldOdds;
   }


    private BetResponse mapBetToBetResponse(Bet bet) {
        return BetResponse.builder()
                .id(bet.getId())
                .matchId(bet.getMatch().getId())
                .odds(bet.getOdds())
                .betType(bet.getBetType())
                .betStatus(bet.getBetStatus())
                .build();
    }

    public void deleteBet (Long id) {
        try {
            Bet bet = betRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Couldn't find bet with id: "+id));
            betRepository.delete(bet);
        }catch (Exception e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public List<Bet> findRelatedBets(BetType bet, Match match){
        try{
            List<Bet> relatedBets = new ArrayList<>();
            if(bet.getBetTypeCode().equals("direct") && bet.getBetStat().equals("score")){
                int firstTeam;
                int secondTeam = switch (bet.getTeam()) {
                    case 0 -> {
                        firstTeam = 1;
                        yield 2;
                    }
                    case 1 -> {
                        firstTeam = 0;
                        yield 2;
                    }
                    case 2 -> {
                        firstTeam = 1;
                        yield 0;
                    }
                    default -> throw new ServiceException("Unsupported team value");
                };
                relatedBets.addAll(betRepository.findByMatchAndBetType_BetStatAndBetType_Team(match,"score",firstTeam));
                relatedBets.addAll(betRepository.findByMatchAndBetType_BetStatAndBetType_Team(match,"score",secondTeam));
                return relatedBets;
            } else if (bet.getBetTypeCode().equals("direct") && (bet.getBetStat().equals("penalties") || bet.getBetStat().equals("redCards"))) {
                Double negativeValue = bet.getTargetValue()==1.0?0.0:1.0;
                relatedBets.addAll(betRepository.findByMatchAndBetType_BetStatAndBetType_TargetValue(match, bet.getBetStat(),negativeValue));
            }
            if (bet.getBetTypeCode().equals("over") || bet.getBetTypeCode().equals("under")){
                relatedBets.addAll(betRepository.findByMatchAndBetType_BetStatAndBetType_TeamAndBetType_TargetValueNot(
                        match,
                        bet.getBetTypeCode(),
                        bet.getTeam(),
                        bet.getTargetValue()
                        ));
            }
            return relatedBets;
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * @param match
     * Betting on match score (team victory):
     * betType: direct
     * team: 0 - homeTeam , 1 - awayTeam
     * targetValue: 1.0 - win or draw , 0.0 - win
     * betStat: score
     * <p>
     * Betting on match score (draw):
     * betType: direct
     * team: 2
     * targetValue: 2.0
     * betStat: score
     * <p>
     * Betting on specific stat
     * betType: over/under, direct - only for penalties and red cards
     * team: 0- homeTeam, 1-awayTeam , 2 - either (only for penalties and red cards)
     * targetValue: bet target value e.g. 2 or more is 1.5 (always ends with .5)
     * betStat: goals, shots, shotsOnTarget,passes, corners
     * <p>
     * Betting on specific stat - EXCEPTIONS:
     * 1. possession - betType can only be over
     * 2. penalties - betType = direct, team=2, targetValue= 1.0 if yes 0.0 if not
     * 3. redCards - betType = direct, team =2, targetValue= 1.0 if yes 0.0 if not
     * 4. yellowCards - betType= over , team =2
     * 5. fouls - betType = over , team=2
     */
    public void updateBetsStatusAfterMatch(Match match){
        try {
            MatchStats homeTeamStats = matchStatsRepository.findByMatchAndTeam(match, match.getHomeTeam().getId()).orElseThrow(()
                    -> new ServiceException("Couldn't find stats for home team"));
            MatchStats awayTeamStats =matchStatsRepository.findByMatchAndTeam(match, match.getAwayTeam().getId()).orElseThrow(()
                    -> new ServiceException("Couldn't find stats for away team"));

            processBets("score", match, bet -> updateScoreBetStatus(bet, homeTeamStats, awayTeamStats));
            processBets("goals", match, bet -> updateGoalsBetStatus(bet, homeTeamStats, awayTeamStats));
            processBets("possession", match, bet -> updateOverBetStatus(bet, "possession", homeTeamStats, awayTeamStats));
            processBets("shots", match, bet -> updateOverUnderBetStatus(bet, "shots", homeTeamStats, awayTeamStats));
            processBets("shotsOnTarget", match, bet -> updateOverUnderBetStatus(bet, "shotsOnTarget", homeTeamStats, awayTeamStats));
            processBets("passes", match, bet -> updateOverUnderBetStatus(bet, "passes", homeTeamStats, awayTeamStats));
            processBets("corners", match, bet -> updateOverUnderBetStatus(bet, "corners", homeTeamStats, awayTeamStats));
            processBets("penalties", match, bet -> updateDirectBetStatus(bet, "penalties", homeTeamStats, awayTeamStats));
            processBets("redCards", match, bet -> updateDirectBetStatus(bet, "redCards", homeTeamStats, awayTeamStats));
            processBets("yellowCards", match, bet -> updateOverBetStatus(bet, "yellowCards", homeTeamStats, awayTeamStats));
            processBets("fouls", match, bet -> updateOverBetStatus(bet, "fouls", homeTeamStats, awayTeamStats));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
    private void processBets(String betStat, Match match, Consumer<Bet> betProcessor) {
        List<Bet> bets = betRepository.findByMatchAndBetType_betStat(match, betStat);
        for (Bet bet : bets) {
            betProcessor.accept(bet);
        }
    }
    public void updateScoreBetStatus (Bet bet, MatchStats homeStats, MatchStats awayStats) {
        if (bet.getBetType().getBetTypeCode().equals("direct")) {
            if (bet.getBetType().getTargetValue() == 1.0) {
                if (bet.getBetType().getTeam() == 0) {  // Home team
                    bet.setBetStatus(homeStats.getGoalsScored() >= awayStats.getGoalsScored() ? 1 : 2);
                } else {  // Away team
                    bet.setBetStatus(homeStats.getGoalsScored() <= awayStats.getGoalsScored() ? 1 : 2);
                }
            } else if (bet.getBetType().getTargetValue() == 0.0) {
                if (bet.getBetType().getTeam() == 0) {  // Home team
                    bet.setBetStatus(homeStats.getGoalsScored() > awayStats.getGoalsScored() ? 1 : 2);
                } else {  // Away team
                    bet.setBetStatus(homeStats.getGoalsScored() < awayStats.getGoalsScored() ? 1 : 2);
                }
            } else if (bet.getBetType().getTargetValue() == 2.0) {
                bet.setBetStatus(homeStats.getGoalsScored().equals(awayStats.getGoalsScored()) ? 1 : 2);
            }
        }
    }
    public void updateGoalsBetStatus (Bet bet, MatchStats homeStats, MatchStats awayStats) {
        if (bet.getBetType().getBetTypeCode().equals("over")) {
            int totalGoals = bet.getBetType().getTeam() == 2 ?
                    homeStats.getGoalsScored() + awayStats.getGoalsScored() :
                    (bet.getBetType().getTeam() == 0 ? homeStats.getGoalsScored() : awayStats.getGoalsScored());
            bet.setBetStatus(totalGoals > bet.getBetType().getTargetValue() ? 1 : 2);
        } else if (bet.getBetType().getBetTypeCode().equals("under")) {
            int totalGoals = bet.getBetType().getTeam() == 2 ?
                    homeStats.getGoalsScored() + awayStats.getGoalsScored() :
                    (bet.getBetType().getTeam() == 0 ? homeStats.getGoalsScored() : awayStats.getGoalsScored());
            bet.setBetStatus(totalGoals < bet.getBetType().getTargetValue() ? 1 : 2);
        }
    }
    public void updateOverBetStatus (Bet bet, String statType, MatchStats homeStats, MatchStats awayStats) {
        int statValue = switch (statType) {
            case "possession" ->
                    bet.getBetType().getTeam() == 0 ? homeStats.getPossession() : awayStats.getPossession();
            case "yellowCards" -> homeStats.getYellowCards() + awayStats.getYellowCards();
            case "fouls" -> homeStats.getFouls() + awayStats.getFouls();
            default -> 0;
        };
        bet.setBetStatus(statValue > bet.getBetType().getTargetValue() ? 1 : 2);
    }
    public void updateDirectBetStatus (Bet bet, String statType, MatchStats homeStats, MatchStats awayStats) {
        int statValue = 0;
        if ("penalties".equals(statType)) {
            statValue = homeStats.getPenalties() + awayStats.getPenalties();
        } else if ("redCards".equals(statType)) {
            statValue = homeStats.getRedCards() + awayStats.getRedCards();
        }
        if (bet.getBetType().getTargetValue() == 1.0) {
            bet.setBetStatus(statValue > 0 ? 1 : 2);
        } else if (bet.getBetType().getTargetValue() == 0.0) {
            bet.setBetStatus(statValue == 0 ? 1 : 2);
        }
    }
    public void updateOverUnderBetStatus (Bet bet, String statType, MatchStats homeStats, MatchStats awayStats) {
        int statValue = switch (statType) {
            case "shots" -> bet.getBetType().getTeam() == 0 ? homeStats.getShots() : awayStats.getShots();
            case "shotsOnTarget" ->
                    bet.getBetType().getTeam() == 0 ? homeStats.getShotsOnTarget() : awayStats.getShotsOnTarget();
            case "passes" -> bet.getBetType().getTeam() == 0 ? homeStats.getPasses() : awayStats.getPasses();
            case "corners" -> bet.getBetType().getTeam() == 0 ? homeStats.getCorners() : awayStats.getCorners();
            default -> 0;
        };
        if (Objects.equals(bet.getBetType().getBetTypeCode(), "over")) {
            bet.setBetStatus(statValue > bet.getBetType().getTargetValue() ? 1 : 2);
        } else if (Objects.equals(bet.getBetType().getBetTypeCode(), "under")) {
            bet.setBetStatus(statValue < bet.getBetType().getTargetValue() ? 1 : 2);
        }
    }
    private Double calculateLiveOdds(Double oldOdds, int betsPlaced){
        //TO:DO - possibly add some logic here
        return null;
    }
}
