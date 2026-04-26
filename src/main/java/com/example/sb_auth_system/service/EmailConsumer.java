package com.example.sb_auth_system.service;

import com.example.sb_auth_system.config.RabbitMQConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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
    private final TemplateEngine templateEngine; // Inject Thymeleaf engine

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(Map<String, String> payload) {
        String email = payload.get("email");
        String type = payload.get("type");
        String data = payload.get("data");

        // 1. Prepare Thymeleaf Context (the data for the HTML)
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

        // 2. Process the template into a String
        String htmlContent = templateEngine.process("email-template", context);

        // 3. Send the HTML email
        sendEmail(email, subject, htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = isHtml

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Log error
        }
    }
}
