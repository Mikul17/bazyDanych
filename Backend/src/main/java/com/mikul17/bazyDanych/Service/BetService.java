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
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            Bet updated =betRepository.findById(bet.getId()).orElseThrow(()
                    -> new ServiceException("Bet not found"));
            int amountOfBets = couponRepository.findByBetsId(bet.getId()).size();
            updated.setOdds(calculateNewOdds(bet.getOdds(), amountOfBets));
            betRepository.save(updated);
        }catch (Exception e){
            throw new ServiceException("Error: " + e.getMessage());
        }
   }

   //Tweak numbers if needed
   private Double calculateNewOdds(Double oldOdds, int betsPlaced){
        return oldOdds - 0.05 * betsPlaced;
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

    private void updateBetsStatusAfterMatch(Match match){
        try{
            MatchStats homeTeamStats = matchStatsRepository.findByIdAndTeam(match.getId(), match.getHomeTeam()).orElseThrow(()
                    -> new ServiceException("Couldn't find stats for home team"));
            MatchStats awayTeamStats =matchStatsRepository.findByIdAndTeam(match.getId(), match.getAwayTeam()).orElseThrow(()
                    -> new ServiceException("Couldn't find stats for away team"));

            //goals
            List<Bet> goalsBets = betRepository.findByMatchAndBetType_betStat(match, "goals");
            for(Bet bet : goalsBets){
                if(Objects.equals(bet.getBetType().getBetTypeCode(), "over")){
                    //if home team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getGoalsScored()>bet.getBetType().getTargetValue()?1:2);
                    }else if (bet.getBetType().getTeam()){
                        bet.setBetStatus(awayTeamStats.getGoalsScored()>bet.getBetType().getTargetValue()?1:2);
                    }else if (bet.getBetType().getTeam() == null){
                        bet.setBetStatus((homeTeamStats.getGoalsScored() + awayTeamStats.getGoalsScored())>bet.getBetType().getTargetValue()?1:2);
                    }else{
                        throw new ServiceException("Unsupported bet type");
                    }
                } else if (Objects.equals(bet.getBetType().getBetTypeCode(), "under")){
                    //if away team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getGoalsScored()<bet.getBetType().getTargetValue()?1:2);
                    }else if(bet.getBetType().getTeam()){
                        bet.setBetStatus(awayTeamStats.getGoalsScored()<bet.getBetType().getTargetValue()?1:2);
                    }else if (bet.getBetType().getTeam() == null){
                        bet.setBetStatus((homeTeamStats.getGoalsScored() + awayTeamStats.getGoalsScored())>bet.getBetType().getTargetValue()?1:2);
                    }else{
                        throw new ServiceException("Unsupported bet type");
                    }
                }
            }
            //possession - can be only over
            List<Bet> possessionBets = betRepository.findByMatchAndBetType_betStat(match, "possession");
            for(Bet bet : possessionBets){
                if(!bet.getBetType().getTeam()){
                    bet.setBetStatus(homeTeamStats.getPossession()>bet.getBetType().getTargetValue()?1:2);
                }else{
                    bet.setBetStatus(awayTeamStats.getPossession()>bet.getBetType().getTargetValue()?1:2);
                }
            }
            //shots
            List<Bet> shotsBets = betRepository.findByMatchAndBetType_betStat(match, "shots");
            for(Bet bet : shotsBets){
                if(Objects.equals(bet.getBetType().getBetTypeCode(), "over")){
                    //if home team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getShots()>bet.getBetType().getTargetValue()?1:2);
                    }else{
                        bet.setBetStatus(awayTeamStats.getShots()>bet.getBetType().getTargetValue()?1:2);
                    }
                } else if (Objects.equals(bet.getBetType().getBetTypeCode(), "under")){
                    //if away team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getShots()<bet.getBetType().getTargetValue()?1:2);
                    }else{
                        bet.setBetStatus(awayTeamStats.getShots()<bet.getBetType().getTargetValue()?1:2);
                    }
                }
            }
            //shots on target
            List<Bet> shotsOnTargetBets = betRepository.findByMatchAndBetType_betStat(match, "shotsOnTarget");
            for(Bet bet : shotsOnTargetBets){
                if(Objects.equals(bet.getBetType().getBetTypeCode(), "over")){
                    //if home team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getShotsOnTarget()>bet.getBetType().getTargetValue()?1:2);
                    }else{
                        bet.setBetStatus(awayTeamStats.getShotsOnTarget()>bet.getBetType().getTargetValue()?1:2);
                    }
                } else if (Objects.equals(bet.getBetType().getBetTypeCode(), "under")){
                    //if away team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getShotsOnTarget()<bet.getBetType().getTargetValue()?1:2);
                    }else{
                        bet.setBetStatus(awayTeamStats.getShotsOnTarget()<bet.getBetType().getTargetValue()?1:2);
                    }
                }
            }
            //passes
            List<Bet> passesBets = betRepository.findByMatchAndBetType_betStat(match, "passes");
            for(Bet bet : passesBets){
                if(Objects.equals(bet.getBetType().getBetTypeCode(), "over")){
                    //if home team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getPasses()>bet.getBetType().getTargetValue()?1:2);
                    }else{
                        bet.setBetStatus(awayTeamStats.getPasses()>bet.getBetType().getTargetValue()?1:2);
                    }
                } else if (Objects.equals(bet.getBetType().getBetTypeCode(), "under")){
                    //if away team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getPasses()<bet.getBetType().getTargetValue()?1:2);
                    }else{
                        bet.setBetStatus(awayTeamStats.getPasses()<bet.getBetType().getTargetValue()?1:2);
                    }
                }
            }
            //corners
            List<Bet> cornersBets = betRepository.findByMatchAndBetType_betStat(match, "corners");
            for(Bet bet : cornersBets){
                if(Objects.equals(bet.getBetType().getBetTypeCode(), "over")){
                    //if home team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getCorners()>bet.getBetType().getTargetValue()?1:2);
                    }else{
                        bet.setBetStatus(awayTeamStats.getCorners()>bet.getBetType().getTargetValue()?1:2);
                    }
                } else if (Objects.equals(bet.getBetType().getBetTypeCode(), "under")){
                    //if away team
                    if(!bet.getBetType().getTeam()){
                        bet.setBetStatus(homeTeamStats.getCorners()<bet.getBetType().getTargetValue()?1:2);
                    }else{
                        bet.setBetStatus(awayTeamStats.getCorners()<bet.getBetType().getTargetValue()?1:2);
                    }
                }
            }
            //throwIns - i don't think it's possible to bet on this
            //freeKicks - i don't think it's possible to bet on this
            //penalties - if there is a penalty in the match
            List<Bet> penaltiesBets = betRepository.findByMatchAndBetType_betStat(match, "penalties");
            for(Bet bet : penaltiesBets){
               if(Objects.equals(bet.getBetType().getBetTypeCode(), "direct")){
                   int penalties = homeTeamStats.getPenalties() + awayTeamStats.getPenalties();
                     bet.setBetStatus(penalties>0?1:2);
               }
            }
            //redCards - if there is a red card in the match
            List<Bet> redCardsBets = betRepository.findByMatchAndBetType_betStat(match, "redCards");
            for(Bet bet : redCardsBets){
                if(Objects.equals(bet.getBetType().getBetTypeCode(), "direct")){
                    int redCards = homeTeamStats.getRedCards() + awayTeamStats.getRedCards();
                    bet.setBetStatus(redCards>0?1:2);
                }
            }

            //yellowCards - only over and only for both teams
            List<Bet> yellowCardsBets = betRepository.findByMatchAndBetType_betStat(match, "yellowCards");
            for(Bet bet : yellowCardsBets){
                if(Objects.equals(bet.getBetType().getBetTypeCode(), "over")){
                    int yellowCards = homeTeamStats.getYellowCards() + awayTeamStats.getYellowCards();
                    bet.setBetStatus(yellowCards>bet.getBetType().getTargetValue()?1:2);
                }
            }

            //fouls - only over and only for both teams
            List<Bet> foulsBets = betRepository.findByMatchAndBetType_betStat(match, "fouls");
            for(Bet bet : foulsBets){
                if(Objects.equals(bet.getBetType().getBetTypeCode(), "over")){
                    int fouls = homeTeamStats.getFouls() + awayTeamStats.getFouls();
                    bet.setBetStatus(fouls>bet.getBetType().getTargetValue()?1:2);
                }
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    //Implement if enough time
    private Double calculateLiveOdds(Double oldOdds, int betsPlaced){
        //TO:DO - possibly add some logic here
        return null;
    }
}
