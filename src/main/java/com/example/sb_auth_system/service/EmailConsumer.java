package com.example.sb_auth_system.service;

import com.example.sb_auth_system.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(Map<String, String> payload) {

        String email = payload.get("email");
        String type = payload.get("type");
        String data = payload.get("data");

        switch (type) {

            case "WELCOME":
                System.out.println("Send welcome email to " + email);
                break;

            case "VERIFY":
                System.out.println("Send verification code " + data + " to " + email);
                break;

            case "RESET_PASSWORD":
                System.out.println("Send reset code " + data + " to " + email);
                break;
        }
    }
}