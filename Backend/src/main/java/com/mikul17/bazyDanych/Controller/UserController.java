package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/fullUserInfo/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(userService.getUserById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInfoById(@PathVariable Long id){
        try{
           return ResponseEntity.ok().body(userService.getUserInfoById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<?> getUserBalance(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(userService.getUserBalanceById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/phoneNumber={pn}_id={id}")
    public ResponseEntity<?> updatePhoneNumber(@PathVariable String pn, @PathVariable Long id){
        try{
            return ResponseEntity.ok().body(userService.changePhoneNumber(pn,id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("upade/accNumber={ac}_id={id}")
    public ResponseEntity<?> updateAccNumber(@PathVariable String ac, @PathVariable Long id){
        try {
            return ResponseEntity.ok().body(userService.changeAccountNumber(ac,id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
