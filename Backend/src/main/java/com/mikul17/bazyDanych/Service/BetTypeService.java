package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Coupons.Bet;
import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Repository.BetTypeRepository;
import com.mikul17.bazyDanych.Request.BetTypeRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BetTypeService {

    private final BetTypeRepository betTypeRepository;

    public String decodeBetFromBetType (BetType betType) {

        if(!isBetValid(betType)){
            throw new ServiceException("Bet type is invalid");
        }

        StringBuilder decoded = new StringBuilder();
        if (betType.getTeam() == 2) {
            if(!Objects.equals(betType.getBetTypeCode(), "direct")){
                decoded.append("There will be ");
                decoded.append(betType.getBetTypeCode().equals("yellowCards")?"yellow cards":betType.getBetTypeCode());
                decoded.append(" ");
                decoded.append(betType.getTargetValue());
                decoded.append(" ");
            }
            if(betType.getBetStat().equals("score")){
                decoded.append("This match will end in a draw");
            }else if (betType.getBetStat().equals("penalties") || betType.getBetStat().equals("redCards")){
                if(betType.getTargetValue()==1.0){
                    decoded.append("There will be ");
                    decoded.append(betType.getBetStat().equals("penalties")? "penalty" : "red card");
                    decoded.append(" in this match");
                }else{
                    decoded.append("There will not be ");
                    decoded.append(betType.getBetStat().equals("penalties")? "penalty" : "red card");
                    decoded.append(" in this match");
                }
            }
        } else {
            if(betType.getTeam()==0){
                decoded.append("Home team");
            }else{
                decoded.append("Away team");
            }
            if (Objects.equals(betType.getBetTypeCode(), "direct")) {
                decoded.append(" will ");
                if(betType.getTargetValue()==1.0){
                    decoded.append("win or draw");
                }else if(betType.getTargetValue()==0.0){
                    decoded.append("win");
                }
                decoded.append(" the match");
            } else {
                decoded.append(" will get ");
                decoded.append(betType.getBetTypeCode().equals("yellowCards")?"yellow cards":betType.getBetTypeCode());
                decoded.append(" ");
                decoded.append(betType.getTargetValue());
                decoded.append(" ");
                decoded.append(betType.getBetStat());
                decoded.append(" in this match");
            }
        }
        return decoded.toString();
    }

    public String decodeById (Long id) {
        try {
            BetType found = betTypeRepository.findById(id).orElseThrow(()
                    -> new ServiceException("Bet type not found"));
            return decodeBetFromBetType(found);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public String addNewBetType (BetTypeRequest betTypeRequest) {
        try {
            BetType betType = BetType.builder()
                    .betStat(betTypeRequest.getBetStat())
                    .betTypeCode(betTypeRequest.getBetTypeCode())
                    .team(betTypeRequest.getTeam())
                    .targetValue(betTypeRequest.getTargetValue())
                    .build();

            if(!isBetValid(betType)){
                throw new ServiceException("Invalid bet type");
            }

            betTypeRepository.save(betType);

            return decodeBetFromBetType(betType);

        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<BetType> getAllBetTypes () {
        try {
            return betTypeRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public BetType getBetTypeById (Long betTypeId) {
        try {
            return betTypeRepository.findById(betTypeId).orElseThrow(
                    () -> new ServiceException("Couldn't find bet type with id: " + betTypeId));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }

    public List<BetType> getBetTypeByStat (String betStat) {
        try {
            return betTypeRepository.findByBetStat(betStat);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<BetType> getBetTypeByCode (String betTypeCode) {
        try {
            return betTypeRepository.findByBetTypeCode(betTypeCode);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<BetType> getBetTypeByCodeAndStatSorted(String betTypeCode, String stat, Boolean asc){
        try{
            if(asc){
                return betTypeRepository.findByBetTypeCodeAndBetStatOrderByTargetValueAsc(betTypeCode,stat);
            }else{
                return betTypeRepository.findByBetTypeCodeAndBetStatOrderByTargetValueDesc(betTypeCode,stat);
            }

        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public List<BetType> getBetTypeByCodeAndStat(String betTypeCode, String stat){
        try{
            return betTypeRepository.findByBetTypeCodeAndBetStat(betTypeCode,stat);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public BetType getBetTypeByTypeAndTeamAndValue(String type, Integer team, Double value){
          return betTypeRepository.findByBetTypeCodeAndTeamAndTargetValue(type,team,value);
    }

    public void deleteBetType (Long betTypeId) {
        try {
            betTypeRepository.findById(betTypeId).orElseThrow(() ->
                    new ServiceException("Couldn't find bet type with id: " + betTypeId));

            betTypeRepository.deleteById(betTypeId);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public String updateBetType (BetTypeRequest betTypeRequest, Long betTypeId) {
        try {
            if (betTypeRequest.getBetStat() == null || betTypeRequest.getBetTypeCode() == null
                    || betTypeRequest.getTargetValue() == null)
                throw new ServiceException("Missing required fields");

            betTypeRepository.findById(betTypeId).orElseThrow(()
                    -> new Exception("Couln't find bet type with id: " + betTypeId));
            BetType betType = BetType.builder()
                    .betStat(betTypeRequest.getBetStat())
                    .betTypeCode(betTypeRequest.getBetTypeCode())
                    .team(betTypeRequest.getTeam())
                    .targetValue(betTypeRequest.getTargetValue())
                    .build();
            betTypeRepository.save(betType);
            return decodeBetFromBetType(betType);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private Boolean isBetValid(BetType bet){
        if(bet.getTeam()<0 || bet.getTeam()>2){
            return false;
        }

        if(!bet.getBetTypeCode().equals("direct") &&  !bet.getBetTypeCode().equals("over") && !bet.getBetTypeCode().equals("under")){
            return false;
        }

        if(bet.getBetTypeCode().equals("direct")){
            if(bet.getTargetValue() != 1.0 && bet.getTargetValue() != 0.0 && bet.getTargetValue() != 2.0){
                return false;
            }
        }

        if(bet.getBetStat().equals("possession") && !bet.getBetTypeCode().equals("over")){
            return false;
        }

        if(bet.getBetStat().equals("penalties") && (!bet.getBetTypeCode().equals("direct") || bet.getTeam()!=2)){
            if(bet.getTargetValue()!=1.0 && bet.getTargetValue()!=0.0){
                return false;
            }
        }

        if(bet.getBetStat().equals("redCards") && (!bet.getBetTypeCode().equals("direct") || bet.getTeam()!=2)){
            if(bet.getTargetValue()!=1.0 && bet.getTargetValue()!=0.0){
                return false;
            }
        }

        if(bet.getBetStat().equals("yellowCards") && (!bet.getBetTypeCode().equals("over") || bet.getTeam()!=2)){
            return false;
        }

        if(bet.getBetStat().equals("fouls") && (!bet.getBetTypeCode().equals("over") || bet.getTeam()!=2)){
            return false;
        }

        if(bet.getBetTypeCode().equals("over") || bet.getBetTypeCode().equals("under")){
            return String.valueOf(bet.getTargetValue()).endsWith(".5");
        }

        return true;
    }

}