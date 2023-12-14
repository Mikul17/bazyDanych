package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Models.Coupons.BetType;
import com.mikul17.bazyDanych.Repository.BetTypeRepository;
import com.mikul17.bazyDanych.Request.BetTypeRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BetTypeService {

    private final BetTypeRepository betTypeRepository;

    public String decodeBetFromBetType (BetType betType) {
        StringBuilder decoded = new StringBuilder();
        if (betType.getTeam() == null) {
            decoded.append("There will be ");
            decoded.append(betType.getBetTypeCode());
            decoded.append(" ");
            decoded.append(betType.getTargetValue());
            decoded.append(" ");
            decoded.append(betType.getBetStat());
            decoded.append(" in this match");
        } else {
            decoded.append(!betType.getTeam() ? "Home" : "Away").append(" team");
            if (Objects.equals(betType.getBetTypeCode(), "direct")) {
                decoded.append(" will ");
                decoded.append(betType.getTargetValue() == (1.1) ? "win or draw" : "win");
                decoded.append(" the match");
            } else {
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
            if(betTypeRequest.getBetStat() == null || betTypeRequest.getBetTypeCode() == null
                    || betTypeRequest.getTargetValue() == null)
                throw new ServiceException("Missing required fields");

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
    public void deleteBetType (Long betTypeId) {
        try {
            betTypeRepository.findById(betTypeId).orElseThrow(() ->
                    new ServiceException("Couldn't find bet type with id: " + betTypeId));

            betTypeRepository.deleteById(betTypeId);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
    public String updateBetType (BetTypeRequest betTypeRequest, Long betTypeId){
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
}