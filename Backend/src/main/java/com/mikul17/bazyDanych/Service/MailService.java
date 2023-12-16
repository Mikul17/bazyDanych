package com.mikul17.bazyDanych.Service;

import com.mikul17.bazyDanych.Request.MailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(MailRequest request) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(request.getEmail());
            mimeMessageHelper.setFrom("noreplay@bet.com");
            mimeMessageHelper.setSubject(request.getSubject());
            mimeMessageHelper.setText(request.getText(), true);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
