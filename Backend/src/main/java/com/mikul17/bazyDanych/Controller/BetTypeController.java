package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.BetTypeRequest;
import com.mikul17.bazyDanych.Service.BetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/betType")
@RequiredArgsConstructor
public class BetTypeController {

    private final BetTypeService betTypeService;

    @PostMapping("/add")
    public ResponseEntity<?> addNewBetType(@RequestBody BetTypeRequest betTypeRequest) {
        try{
            return ResponseEntity.ok().body(betTypeService.addNewBetType(betTypeRequest));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error adding new bet type: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBetType(@PathVariable Long id, @RequestBody BetTypeRequest betTypeRequest) {
        try{
            return ResponseEntity.ok().body(betTypeService.updateBetType(betTypeRequest,id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error updating bet type: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBetType(@PathVariable Long id) {
        try {
            betTypeService.deleteBetType(id);
            return ResponseEntity.ok().body("Bet type deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting bet type: " + e.getMessage());
        }
    }

    @GetMapping("/decode/{id}")
    public ResponseEntity<?> decodeBetType(@PathVariable Long id) {
        try{
            return ResponseEntity.ok().body(betTypeService.decodeById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error decoding bet type: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBetTypes() {
        try{
            return ResponseEntity.ok().body(betTypeService.getAllBetTypes());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error getting all bet types: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBetTypeById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok().body(betTypeService.getBetTypeById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error getting bet type by id: " + e.getMessage());
        }
    }

    @GetMapping("/byStat/{stat}")
    public ResponseEntity<?> getBetTypeByStat(@PathVariable String stat) {
        try{
            return ResponseEntity.ok().body(betTypeService.getBetTypeByStat(stat));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error getting bet type by stat: " + e.getMessage());
        }
    }

    @GetMapping("/byCode/{code}")
    public ResponseEntity<?> getBetTypeByCode(@PathVariable String code) {
        try{
            return ResponseEntity.ok().body(betTypeService.getBetTypeByCode(code));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error getting bet type by code: " + e.getMessage());
        }
    }

}
