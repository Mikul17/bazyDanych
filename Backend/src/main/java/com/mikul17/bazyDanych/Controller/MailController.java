package com.mikul17.bazyDanych.Controller;

import com.mikul17.bazyDanych.Request.MailRequest;
import com.mikul17.bazyDanych.Service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class MailController {
    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@RequestBody MailRequest request) {
       try{
           mailService.sendMail(request);
           return ResponseEntity.ok().body("Mail sent successfully");
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
}
