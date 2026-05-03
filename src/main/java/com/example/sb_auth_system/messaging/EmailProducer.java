package com.example.sb_auth_system.messaging;

import com.example.sb_auth_system.config.RabbitMQConfig;
import com.example.sb_auth_system.dto.email.EmailMessage;
import com.example.sb_auth_system.dto.email.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

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