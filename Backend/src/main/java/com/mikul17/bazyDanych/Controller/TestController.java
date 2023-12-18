package com.mikul17.bazyDanych.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/userOnly")
    public ResponseEntity<?> userOnly(){
        try{
            return ResponseEntity.ok().body("User only endpoint");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> allAccess(){
        try{
            return ResponseEntity.ok().body("All privilege access endpoint");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> adminOnly(){
        try{
            return ResponseEntity.ok().body("Admin only endpoint");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/any")
    public ResponseEntity<?> anyPrivilege(){
        try{
            return ResponseEntity.ok().body("User and admin only privilege");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
