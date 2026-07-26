package com.project.quiz_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(
            String to,
            String name,
            String resetLink
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Quiz System - Reset Password");

        message.setText("""
Hello %s,

We received a request to reset your password.

Click the link below to reset your password:

%s

This link will expire in 30 minutes.

If you did not request this, you can safely ignore this email.

Regards,
Quiz System Team
""".formatted(name, resetLink));

        mailSender.send(message);
    }
}