package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.BetTypeRequest;
import com.mikul17.bazyDanych.Response.BetTypeResponse;
import com.mikul17.bazyDanych.Service.BetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/betType")
public class BetTypeController {

    @Autowired
    private  BetTypeService betTypeService;

    @PostMapping("/add")
    public BetTypeResponse addNewBetType(@RequestBody BetTypeRequest betTypeRequest) {
        return betTypeService.addNewBetType(betTypeRequest);
    }
}
