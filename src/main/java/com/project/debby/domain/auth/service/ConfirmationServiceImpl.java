package com.project.debby.domain.auth.service;

import com.project.debby.domain.auth.model.ConfirmationMail;
import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.util.exceptions.RequestedEntityNotFound;
import com.project.debby.util.service.JwtService;
import com.project.debby.util.service.mailService.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;

@Log4j2
@RequiredArgsConstructor
@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    @Value("${server.base-url}")
    private String baseURL;

    @Value("${mail.confirmation_hash_lifetime}")
    private int confirmationLifetime;

    private final UserDetailsService userDetailsService;
    private final JwtService jwtUtil;
    private final MailService mailService;

    @Override
    public ConfirmationMail sendConfirmation(String email, String userID) {
        ConfirmationMail link = createConfirmationMail(userID);
        String subject = "Confirmation Link";
        String text = "To verify this email and activate your account" +
                " please follow this confirmation link " + baseURL + "users/confirm/"
                + link.getHash();
        log.info("--sending confirmation mail | user extId {}", userID);
        mailService.sendMail(email, subject, text);
        return link;
    }

    public ConfirmationMail createConfirmationMail(String userID) {
        String hash = jwtUtil.createConfirmationToken(new HashMap<>(), userID, confirmationLifetime);
        ConfirmationMail link = new ConfirmationMail();
        link.setHash(hash);
        link.setExpireDate(LocalDate.now());
        link.setExternalID(userID);
        return link;
    }

    @Override
    public UserDetails checkConfirmation(String hash) {
        UserDetails userDetails = null;
        if(!jwtUtil.isTokenExpired(hash)){
            try {
                String userID = jwtUtil.extractUserIdFromToken(hash);
                userDetailsService.enableUser(userID);
                userDetails = (UserDetails) userDetailsService.loadUserByUsername(userID);
            }
            catch (RequestedEntityNotFound e){
                e.printStackTrace();
            }
        }
        return userDetails;
    }
}
