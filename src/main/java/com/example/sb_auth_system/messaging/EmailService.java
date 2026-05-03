package com.example.sb_auth_system.messaging;

import com.example.sb_auth_system.dto.email.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void handleEmail(EmailMessage message) {

        String template;
        String subject;
        Map<String, Object> vars = new HashMap<>();

        vars.put("name", message.getUsername());
        vars.put("email", message.getEmail());

        switch (message.getType()) {

            case WELCOME -> {
                template = "email/welcome";
                subject = "Welcome to the platform 🎉";
                vars.put("message", "We are happy to have you!");
            }

            case VERIFY -> {
                template = "email/verify";
                subject = "Verify your account";
                vars.put("code", message.getData());
            }

            case RESET_PASSWORD -> {
                template = "email/reset-password";
                subject = "Reset your password";
                vars.put("code", message.getData());
            }

            default -> throw new IllegalArgumentException("Invalid email type");
        }

        sendEmail(message.getEmail(), subject, template, vars);
    }

    private void sendEmail(String to, String subject, String template, Map<String, Object> variables) {

        Context context = new Context();
        context.setVariables(variables);

        String html = templateEngine.process(template, context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}