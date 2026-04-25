package com.example.sb_auth_system.service;

import com.example.sb_auth_system.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(Map<String, String> payload) {
        String email = payload.get("email");
        String type = payload.get("type");
        String data = payload.get("data");

        String subject;
        String body;

        switch (type) {
            case "WELCOME":
                subject = "Welcome to Our Service!";
                body = "Hello! We are excited to have you with us.";
                break;

            case "VERIFY":
                subject = "Your Verification Code";
                body = "Your verification code is: " + data;
                break;

            case "RESET_PASSWORD":
                subject = "Password Reset Request";
                body = "Use this code to reset your password: " + data;
                break;

            default:
                return;
        }

        sendEmail(email, subject, body);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
