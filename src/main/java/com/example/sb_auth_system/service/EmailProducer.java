package com.example.sb_auth_system.service;

import com.example.sb_auth_system.config.RabbitMQConfig;
import com.example.sb_auth_system.dto.EmailMessage;
import com.example.sb_auth_system.dto.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(String email, String username, EmailType type, String data) {

        EmailMessage message = new EmailMessage(email, username, type, data);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}