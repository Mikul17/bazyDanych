package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.ChangePasswordRequest;
import com.mikul17.bazyDanych.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @PutMapping("/update/")
    public ResponseEntity<?> updatePhoneNumber(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("userId") Long userId){
        try{
            return ResponseEntity.ok().body(userService.changePhoneNumber(phoneNumber,userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAccNumber(@RequestParam("accountNumber") String accountNumber, @RequestParam("userId") Long userId){
        try {
            return ResponseEntity.ok().body(userService.changeAccountNumber(accountNumber,userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/ban")
    public ResponseEntity<?> banUser(@RequestParam("userId") Optional<String> userId){
        try{
            return ResponseEntity.ok().body(userService.banUserById(userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
        try{
            userService.changePassword(request);
            return ResponseEntity.ok().body("Password changed");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }

    @PutMapping("/resetRequest")
    public ResponseEntity<?> requestResetPassword (@RequestParam("email") Optional<String> userEmail){
        try{
            userService.resetPasswordProcedure(userEmail);
            return ResponseEntity.ok().body("Check your email to reset your password");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }
    @GetMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("token") Optional<String> passwordToken){
        try{
            userService.resetPasswordForUser(passwordToken);
            return ResponseEntity.ok().body("Password has been changed, check your email");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }

}
