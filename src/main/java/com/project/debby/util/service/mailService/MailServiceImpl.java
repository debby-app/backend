package com.project.debby.util.service.mailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Log4j2
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    @Value("${mail.username}")
    private String username;
    private final JavaMailSender mailSender;
    private final ExecutorService mailThreadPool = Executors.newFixedThreadPool(18);

    @Override
    public boolean sendMail(String destinationEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(destinationEmail);
        message.setSubject(subject);
        message.setText(text);
        log.info("--sending message | destinationEmail {}", destinationEmail);
        mailThreadPool.submit(() -> {
            try {
                mailSender.send(message);
            }
            catch (MailException exception){
                log.error(" Failed to send message to: " + destinationEmail
                    + " with subject: " + subject + "\n Text: " + text + "\nReason: " + exception.getMessage());
            }
        });
        return true;
    }
}
