package com.project.debby.util.service.mailService;

public interface MailService {

    boolean sendMail(String destinationEmail, String subject, String text);
}
