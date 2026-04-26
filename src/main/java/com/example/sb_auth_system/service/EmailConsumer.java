package com.example.sb_auth_system.service;

import com.example.sb_auth_system.config.RabbitMQConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(Map<String, String> payload) {
        String email = payload.get("email");
        String type = payload.get("type");
        String data = payload.get("data");

        Context context = new Context();
        String subject;

        switch (type) {
            case "WELCOME":
                subject = "Welcome!";
                context.setVariable("title", "Welcome to the Platform!");
                context.setVariable("userEmail", email);
                context.setVariable("message", "We are thrilled to have you here.");
                break;
            case "VERIFY":
                subject = "Your Verification Code";
                context.setVariable("title", "Verify Your Identity");
                context.setVariable("userEmail", email);
                context.setVariable("message", "Please use the code below:");
                context.setVariable("data", data);
                break;
            default: return;
        }

        String htmlContent = templateEngine.process("email-template", context);

        sendEmail(email, subject, htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {} due to error: {}", to, e.getMessage());
        }
    }
}
