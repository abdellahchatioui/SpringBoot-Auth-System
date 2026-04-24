package com.example.sb_auth_system.service;

import com.example.sb_auth_system.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(String email, String type, String data) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("type", type);
        payload.put("data", data);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                payload
        );
    }
}