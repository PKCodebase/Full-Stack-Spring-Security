package com.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${spring.mail.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String name) {
        try {
            System.out.println("Sending welcome email to " + toEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // make sure it's not null!
            message.setTo(toEmail);
            message.setSubject("Welcome to Pioneer Solution");
            message.setText("Hello " + name + ",\n\nThanks for registering with us!");
            mailSender.send(message);
            System.out.println("Email sent successfully");
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            // Optional
        }
    }

    public void  sendResetOtpEmail(String toEmail,String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromEmail);
        message.setSubject("Password Reset OTP");
        message.setText("Your Otp for resetting you password is " + otp + " . Use this otp to proceed with resetting your password ");
        mailSender.send(message);
    }
}
