package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.AuthenticationRequest;
import com.mikul17.bazyDanych.Request.RegisterRequest;
import com.mikul17.bazyDanych.Security.Service.AuthService;
import com.mikul17.bazyDanych.Security.UserAlreadyExistAuthenticationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("Registration Successful");
        }catch (ValidationException valE){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Incorrect values");
        } catch (UserAlreadyExistAuthenticationException uaeEx) {
            return ResponseEntity.badRequest().body(uaeEx.getMessage());
        } catch (AuthenticationServiceException asEx) {
            return ResponseEntity.internalServerError().body(asEx.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(authService.authenticate(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String verificationToken){
        try{
            authService.verifyUserByToken(verificationToken);
            return ResponseEntity.ok().body("User verified successfully");
        }catch (AuthenticationServiceException asEx){
            return ResponseEntity.badRequest().body(asEx.getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}