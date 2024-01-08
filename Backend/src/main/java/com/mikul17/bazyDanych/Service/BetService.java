package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.BetStat;
import com.mikul17.bazyDanych.Models.BetTypeCode;
import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Matches.MatchStats;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import com.mikul17.bazyDanych.Repository.*;
import com.mikul17.bazyDanych.Request.BetRequest;
import com.mikul17.bazyDanych.Response.BetResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;
    private final MatchService matchService;
    private final MatchStatsRepository matchStatsRepository;
    private final BetTypeService betTypeService;
    private final CouponRepository couponRepository;
    private final TeamService teamService;

    public BetResponse createBet(BetRequest betRequest) {
        try {
            Match match = matchService.getMatchById(betRequest.getMatchId());
            BetType betType = betTypeService.getBetTypeById(betRequest.getBetTypeId());
            Bet bet = Bet.builder()
                        .match(match)
                        .odds(betRequest.getOdds())
                        .betType(betType)
                        .betStatus(0)
                        .build();
            betRepository.save(bet);
            return mapBetToBetResponse(bet);
        }catch (Exception e) {
            throw new ServiceException(e.getMessage());
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

    public List<BetResponse> getBetsByMatchId(Long matchId) {
        try{
            Match match = matchService.getMatchById(matchId);
            return betRepository.findByMatch(match).stream().map(this::mapBetToBetResponse).collect(Collectors.toList());
        }catch (Exception e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    public BetResponse updateBet (Long id, BetRequest betRequest) {
        try {
            Bet bet = betRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Bet not found"));
            Match match = matchService.getMatchById(betRequest.getMatchId());
            BetType betType = betTypeService.getBetTypeById(betRequest.getBetTypeId());
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

            for (Bet relatedBet : relatedBets) {
                boolean increaseOdds = isIncreaseOdds(bet, relatedBet);
                relatedBet.setOdds(calculateNewOdds(relatedBet.getOdds(), amountOfBets, increaseOdds));
            }

            betRepository.save(bet);
        }catch (Exception e){
            throw new ServiceException("Error: " + e.getMessage());
        }
   }

    private static boolean isIncreaseOdds (Bet bet, Bet relatedBet) {
        boolean increaseOdds;
        if (bet.getBetType().getBetTypeCode().equals(BetTypeCode.OVER.getCode())) {
            // If related bet's target value is lower/equal to the placed bet's target value, decrease odds
            increaseOdds = relatedBet.getBetType().getTargetValue() <= bet.getBetType().getTargetValue();
        } else if (bet.getBetType().getBetTypeCode().equals(BetTypeCode.UNDER.getCode())) {
            // If related bet's target value is higher/equal to the placed bet's target value, decrease odds
            increaseOdds = relatedBet.getBetType().getTargetValue() >= bet.getBetType().getTargetValue();
        } else {
            increaseOdds = true;
        }
        return increaseOdds;
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
                .homeTeam(bet.getMatch().getHomeTeam().getTeamName())
                .awayTeam(bet.getMatch().getAwayTeam().getTeamName())
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

    public List<Bet>  findRelatedBets(BetType bet, Match match){
        try{
            List<Bet> relatedBets = new ArrayList<>();
            if(bet.getBetTypeCode().equals(BetTypeCode.DIRECT.getCode()) && bet.getBetStat().equals("score")){
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
            } else if (bet.getBetTypeCode().equals(BetTypeCode.DIRECT.getCode()) && (bet.getBetStat().equals("penalties") || bet.getBetStat().equals("redCards"))) {
                Double negativeValue = bet.getTargetValue()==1.0?0.0:1.0;
                relatedBets.addAll(betRepository.findByMatchAndBetType_BetStatAndBetType_TargetValue(match, bet.getBetStat(),negativeValue));
            }
            if (bet.getBetTypeCode().equals(BetTypeCode.OVER.getCode()) || bet.getBetTypeCode().equals(BetTypeCode.UNDER.getCode())){
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

            processBets(BetStat.SCORE.getStat(), match, bet -> updateScoreBetStatus(bet, homeTeamStats, awayTeamStats));
            processBets(BetStat.GOALS.getStat(), match, bet -> updateGoalsBetStatus(bet, homeTeamStats, awayTeamStats));
            processBets(BetStat.POSSESSION.getStat(), match, bet -> updateOverBetStatus(bet, BetStat.POSSESSION.getStat(), homeTeamStats, awayTeamStats));
            processBets(BetStat.SHOTS.getStat(), match, bet -> updateOverUnderBetStatus(bet, BetStat.SHOTS.getStat(), homeTeamStats, awayTeamStats));
            processBets(BetStat.SHOTS_ON_TARGET.getStat(), match, bet -> updateOverUnderBetStatus(bet, BetStat.SHOTS_ON_TARGET.getStat(), homeTeamStats, awayTeamStats));
            processBets(BetStat.PASSES.getStat(), match, bet -> updateOverUnderBetStatus(bet, BetStat.PASSES.getStat(), homeTeamStats, awayTeamStats));
            processBets(BetStat.CORNERS.getStat(), match, bet -> updateOverUnderBetStatus(bet, BetStat.CORNERS.getStat(), homeTeamStats, awayTeamStats));
            processBets(BetStat.PENALTIES.getStat(), match, bet -> updateDirectBetStatus(bet, BetStat.PENALTIES.getStat(), homeTeamStats, awayTeamStats));
            processBets(BetStat.RED_CARDS.getStat(), match, bet -> updateDirectBetStatus(bet, BetStat.RED_CARDS.getStat(), homeTeamStats, awayTeamStats));
            processBets(BetStat.YELLOW_CARDS.getStat(), match, bet -> updateOverBetStatus(bet, BetStat.YELLOW_CARDS.getStat(), homeTeamStats, awayTeamStats));
            processBets(BetStat.FOULS.getStat(), match, bet -> updateOverBetStatus(bet, BetStat.FOULS.getStat(), homeTeamStats, awayTeamStats));
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
        if (bet.getBetType().getBetTypeCode().equals(BetTypeCode.DIRECT.getCode())) {
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
        if (bet.getBetType().getBetTypeCode().equals(BetTypeCode.OVER.getCode())) {
            int totalGoals = bet.getBetType().getTeam() == 2 ?
                    homeStats.getGoalsScored() + awayStats.getGoalsScored() :
                    (bet.getBetType().getTeam() == 0 ? homeStats.getGoalsScored() : awayStats.getGoalsScored());
            bet.setBetStatus(totalGoals > bet.getBetType().getTargetValue() ? 1 : 2);
        } else if (bet.getBetType().getBetTypeCode().equals(BetTypeCode.UNDER.getCode())) {
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
        if (Objects.equals(bet.getBetType().getBetTypeCode(), BetTypeCode.OVER.getCode())) {
            bet.setBetStatus(statValue > bet.getBetType().getTargetValue() ? 1 : 2);
        } else if (Objects.equals(bet.getBetType().getBetTypeCode(), BetTypeCode.UNDER.getCode())) {
            bet.setBetStatus(statValue < bet.getBetType().getTargetValue() ? 1 : 2);
        }
    }
    private Double calculateLiveOdds(Double oldOdds, int betsPlaced){
        //TO:DO - possibly add some logic here
        return null;
    }
    public List<Match> getAllMatchesWithoutBets (){
        List<Match> all = matchService.getAllMatches();
        return all.stream()
                .filter(this::matchHasNoBets)
                .collect(Collectors.toList());
    }

    private boolean matchHasNoBets(Match match) {
        return getBetsByMatchId(match.getId()).isEmpty();
    }

    public void generateBetsFromBetTypesForAllMatches(){
        List<Match> matches = getAllMatchesWithoutBets();
        List<BetStat> possibleStats = new ArrayList<>(Arrays.asList(
                BetStat.GOALS,
                BetStat.SHOTS,
                BetStat.SHOTS_ON_TARGET,
                BetStat.PASSES,
                BetStat.CORNERS,
                BetStat.YELLOW_CARDS,
                BetStat.FOULS,
                BetStat.POSSESSION
        ));

        for(Match match : matches){
            generateOverBets(match,possibleStats);
            generateUnderBets(match,possibleStats);
            generateDirectBets(match);
            generateScoreBets(match);
            }
    }

    private void generateOverBets (Match match, List<BetStat> possibleStats) {
        for(BetStat stat : possibleStats) {
            List<BetType> overBets = betTypeService.getBetTypeByCodeAndStatSorted(BetTypeCode.OVER.getCode(),stat.getStat(),true);
            Double prevOdds = 1.35;
            for(BetType type : overBets){
                BetRequest request = BetRequest.builder()
                        .betTypeId(type.getId())
                        .matchId(match.getId())
                        .odds(prevOdds)
                        .build();
                prevOdds=calculateOverUnderOdds(prevOdds);
                createBet(request);
            }
        }
    }

    private void generateUnderBets(Match match, List<BetStat> possibleStats){
        for (BetStat stat : possibleStats){
            List<BetType> underBets = betTypeService.getBetTypeByCodeAndStatSorted(BetTypeCode.UNDER.getCode(),stat.getStat(),false);
            Double prevOdds= 1.35;
            for(BetType type : underBets) {
                BetRequest request = BetRequest.builder()
                        .betTypeId(type.getId())
                        .matchId(match.getId())
                        .odds(prevOdds)
                        .build();
                prevOdds = calculateOverUnderOdds(prevOdds);
                createBet(request);
            }
        }
    }

    private void generateDirectBets(Match match){
        List<BetType> directBets = new ArrayList<>();
        directBets.addAll(betTypeService.getBetTypeByCodeAndStat(BetTypeCode.DIRECT.getCode(),"penalties"));
        directBets.addAll(betTypeService.getBetTypeByCodeAndStat(BetTypeCode.DIRECT.getCode(),"redCards"));
        for(BetType betType : directBets){
            BetRequest request = BetRequest.builder()
                    .betTypeId(betType.getId())
                    .matchId(match.getId())
                    .odds(1.85)
                    .build();
            createBet(request);
        }
    }

    private void generateScoreBets(Match match){
        try {
            Team home = match.getHomeTeam();
            Team away = match.getAwayTeam();
            BetType hWin = betTypeService.getBetTypeByTypeAndTeamAndValue(BetTypeCode.DIRECT.getCode(), 0, 0.0);
            BetType hWinOrDraw = betTypeService.getBetTypeByTypeAndTeamAndValue(BetTypeCode.DIRECT.getCode(), 0, 1.0);
            BetType aWin = betTypeService.getBetTypeByTypeAndTeamAndValue(BetTypeCode.DIRECT.getCode(), 1, 0.0);
            BetType aWinOrDraw = betTypeService.getBetTypeByTypeAndTeamAndValue(BetTypeCode.DIRECT.getCode(), 1, 1.0);
            BetType draw = betTypeService.getBetTypeByTypeAndTeamAndValue(BetTypeCode.DIRECT.getCode(), 2, 2.0);

            int diff = teamService.getDifferenceInRelativePositionBetweenTeams(home, away);
            double oddsFactor = Math.pow(0.9, diff - 1);

            double hTeamOdds = 2 * oddsFactor;
            double aTeamOdds = 2 * (1 / oddsFactor);

            if (teamService.getRelativePositionInLeagueTable(home) > teamService.getRelativePositionInLeagueTable(away)) {
                double temp = hTeamOdds;
                hTeamOdds = aTeamOdds;
                aTeamOdds = temp;
            }

            if(hWin != null){
                createBet(createBetRequest(hWin.getId(), match.getId(), hTeamOdds));
            }
            if(hWinOrDraw != null){
                createBet(createBetRequest(hWinOrDraw.getId(), match.getId(), (hTeamOdds - (0.25 * hTeamOdds))));
            }
            if(aWin!=null){
                createBet(createBetRequest(aWin.getId(), match.getId(), aTeamOdds));
            }
            if(aWinOrDraw!=null){
                createBet(createBetRequest(aWinOrDraw.getId(), match.getId(), (aTeamOdds - (0.25 * aTeamOdds))));
            }
           if(draw !=null){
               createBet(createBetRequest(draw.getId(), match.getId(), 2.25));
           }
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    private Double calculateOverUnderOdds(Double prev){
        return prev+(prev*0.25);
    }

    private BetRequest createBetRequest(Long betTypeId, Long matchId, Double odds){
        return BetRequest.builder()
                .betTypeId(betTypeId)
                .matchId(matchId)
                .odds(odds)
                .build();
    }

    public List<Bet> getByListId (List<Long> betIds) {
        List<Bet> bets = new ArrayList<>();
        for(Long id : betIds){
            bets.add(betRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Bet not found")));
        }
        return bets;
    }
}
