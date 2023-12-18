package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.Coupon;
import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.User;
import com.mikul17.bazyDanych.Repository.CouponRepository;
import com.mikul17.bazyDanych.Repository.MatchRepository;
import com.mikul17.bazyDanych.Request.CouponRequest;
import com.mikul17.bazyDanych.Request.TransactionRequest;
import com.mikul17.bazyDanych.Response.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private static final Logger log = LoggerFactory.getLogger(CouponService.class);

    private final CouponRepository couponRepository;
    private final UserService userService;
    private final BetService betService;
    private final TransactionService transactionService;
    private final MatchService matchService;

    public List<CouponResponse> getAllCoupons () {
        try {
            return couponRepository.findAll().stream().map(this::mapCouponToCouponResponse)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<CouponResponse> getAllCouponsByUserId (Long userId) {
        try {
            userService.checkIfUserExists(userId);
            User user = userService.getUserById(userId);
            return couponRepository.findAllByUser(user).stream().map(this::mapCouponToCouponResponse)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public CouponResponse getCouponById (Long couponId) {
        try {
            return mapCouponToCouponResponse(couponRepository.findById(couponId)
                    .orElseThrow(()
                            -> new ServiceException("Coupon with id: " + couponId + " not found")));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<CouponResponse> getCouponByStatus (Long userId, String couponStatus) {
        try {
            userService.checkIfUserExists(userId);
            User user = userService.getUserById(userId);
            return couponRepository.findByUserAndCouponStatus(user, couponStatus)
                    .stream().map(this::mapCouponToCouponResponse).toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public CouponResponse addCoupon (CouponRequest couponRequest) {
        try {
            List<Bet> bets = betService.getByListId(couponRequest.getBets());
            Double stake = couponRequest.getStake();
            Double totalOdds = calculateTotalOdds(bets);
            Double possibleWin = stake * totalOdds;

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            for (Bet bet : bets) {
                Timestamp matchDate = bet.getMatch().getMatchDate();
                if (matchDate != null && matchDate.before(currentTime)) {
                    throw new ServiceException("Cannot add coupon for matches that have already started.");
                }
            }

            Coupon coupon = Coupon.builder()
                    .creationDate(new Timestamp(System.currentTimeMillis()))
                    .totalOdds(totalOdds)
                    .couponStatus("ACTIVE")
                    .possibleWin(possibleWin)
                    .stake(stake)
                    .user(userService.getUserById(couponRequest.getUserId()))
                    .bets(bets)
                    .build();

            couponRepository.save(coupon);

            //After adding coupon, update odds for other players to bet on
            for (Bet bet : bets) {
                betService.updateOdds(bet);
            }

            return mapCouponToCouponResponse(coupon);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Coupon> getCouponByBetId(Long betId){
        try{
            return couponRepository.findByBetsId(betId);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteCoupon (Long couponId) {
        try {
            couponRepository.findById(couponId).orElseThrow(() ->
                    new ServiceException("Coupon with id: " + couponId + " not found"));
            couponRepository.deleteById(couponId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Error: " + e.getMessage());
        }
    }

    private Double calculateTotalOdds (List<Bet> bets) {
        try {
            Double totalOdds = 1.0;
            for (Bet bet : bets) {
                totalOdds *= bet.getOdds();
            }
            return totalOdds;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public void updateCouponsAfterMatch (Long matchId) {
        try {
            Match match = matchService.getMatchById(matchId);
            log.info("Checking coupons after match between: "
                    + match.getHomeTeam().getTeamName()
                    + " and "
                    + match.getAwayTeam().getTeamName());

            List<Coupon> coupons = couponRepository.findAllByBets_Match(match);
            int winCounter = 0;
            for (Coupon coupon : coupons) {
                for (Bet bet : coupon.getBets()) {
                    if (bet.getBetStatus() == 2) {
                        coupon.setCouponStatus("LOST");
                        break;
                    }
                    if (bet.getBetStatus() == 1) {
                        winCounter++;
                    }
                }
                if (coupon.getCouponStatus().equals("ACTIVE") && winCounter == coupon.getBets().size()) {
                    coupon.setCouponStatus("WON");
                    TransactionRequest trans = TransactionRequest.builder()
                            .type("PAYOUT")
                            .amount(coupon.getPossibleWin())
                            .userId(coupon.getUser().getId())
                            .build();
                    transactionService.performTransaction(trans);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    private CouponResponse mapCouponToCouponResponse (Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .creationDate(coupon.getCreationDate().toString())
                .totalOdds(coupon.getTotalOdds())
                .couponStatus(coupon.getCouponStatus())
                .possibleWin(coupon.getPossibleWin())
                .stake(coupon.getStake())
                .userId(coupon.getUser().getId())
                .bets(coupon.getBets())
                .build();
    }
}
