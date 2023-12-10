package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Repository.BetTypeRepository;
import com.mikul17.bazyDanych.Request.BetTypeRequest;
import com.mikul17.bazyDanych.Response.BetTypeResponse;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BetTypeService {

    private final BetTypeRepository betTypeRepository;

    public String decodeBetFromBetType (BetType betType){
        StringBuilder decoded = new StringBuilder();
        if (betType.getTeam() == null){
            decoded.append("There will be ");
            decoded.append(betType.getBetTypeCode());
            decoded.append(" ");
            decoded.append(betType.getTargetValue());
            decoded.append(" ");
            decoded.append(betType.getBetStat());
            decoded.append(" in this match");
        }else{
            decoded.append(!betType.getTeam() ? "Home" : "Away").append(" team");
            if(Objects.equals(betType.getBetTypeCode(), "direct")){
                decoded.append(" will ");
                decoded.append(betType.getTargetValue() == (1.1) ? "win or draw" : "win");
                decoded.append(" the match");
            }else{
                decoded.append(" will get ");
                decoded.append(betType.getBetTypeCode());
                decoded.append(" ");
                decoded.append(betType.getTargetValue());
                decoded.append(" ");
                decoded.append(betType.getBetStat());
                decoded.append(" in this match");
            }
        }
        return decoded.toString();
    }

    public BetTypeResponse addNewBetType (BetTypeRequest betTypeRequest) {
        try {
            BetType betType = BetType.builder()
                    .betStat(betTypeRequest.getBetStat())
                    .betTypeCode(betTypeRequest.getBetTypeCode())
                    .team(betTypeRequest.getTeam())
                    .targetValue(betTypeRequest.getTargetValue())
                    .build();

            return new BetTypeResponse().builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .message("Bet type added successfully")
                    .decodedBetType(decodeBetFromBetType(betType))
                    .build();

        } catch (Exception e){
            return new BetTypeResponse().builder()
                    .responseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                    .message("Error while adding new bet type")
                    .decodedBetType(null)
                    .build();
        }
    }
}
