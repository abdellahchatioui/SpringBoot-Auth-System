package com.example.sb_auth_system.messaging;

import com.example.sb_auth_system.config.RabbitMQConfig;
import com.example.sb_auth_system.dto.email.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(EmailMessage message) {

        try {
            emailService.handleEmail(message);
        } catch (Exception e) {
            log.error("Failed to process email message: {}", message, e);
        }
    }
}
