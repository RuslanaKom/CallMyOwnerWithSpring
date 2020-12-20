package com.call.my.owner.services;

import com.call.my.owner.repository.UnconfirmedEmailRepository;
import com.call.my.owner.repository.UserRepository;
import com.call.my.owner.entities.UnconfirmedEmail;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.ConfirmationNotFoundException;
import com.call.my.owner.exceptions.UserNotFoundException;
import com.call.my.owner.security.JwtTokenProvider;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationService {

    private static final String CONFIRMATION_EMAIL_TEXT = "Please confirm you email by clicking the link http://localhost:4200/confirm/";
    private static final String CONFIRMATION_EMAIL_SUBJECT = "Confirm you registration";

    private final UnconfirmedEmailRepository unconfirmedEmailRepository;
    private final UserRepository userRepository;
    private final SpringMailSender springMailSender;
    private final JwtTokenProvider tokenProvider;

    public ConfirmationService(UnconfirmedEmailRepository unconfirmedEmailRepository, UserRepository userRepository, SpringMailSender springMailSender, JwtTokenProvider tokenProvider) {
        this.unconfirmedEmailRepository = unconfirmedEmailRepository;
        this.userRepository = userRepository;
        this.springMailSender = springMailSender;
        this.tokenProvider = tokenProvider;
    }

    public void sendConfirmationEmail(ObjectId userId, String email) {
        UnconfirmedEmail unconfirmedEmail = createUnconfirmedEmail(userId, email);
        String fullMessage = CONFIRMATION_EMAIL_TEXT + unconfirmedEmail.getId();
        springMailSender.sendMessage(email, CONFIRMATION_EMAIL_SUBJECT, fullMessage);
    }

    private UnconfirmedEmail createUnconfirmedEmail(ObjectId userId, String email) {
        UnconfirmedEmail unconfirmedEmail = new UnconfirmedEmail();
        unconfirmedEmail.setUserId(userId);
        unconfirmedEmail.setEmail(email);
        unconfirmedEmail = unconfirmedEmailRepository.save(unconfirmedEmail);
        return unconfirmedEmail;
    }

    public String confirmEmail(String unconfirmedEmailId) throws ConfirmationNotFoundException, UserNotFoundException {
        UnconfirmedEmail unconfirmedEmail = unconfirmedEmailRepository.findById(new ObjectId(unconfirmedEmailId))
                .orElseThrow(ConfirmationNotFoundException::new);
        UserAccount userAccount = userRepository.findById(unconfirmedEmail.getUserId())
                .orElseThrow(UserNotFoundException::new);
        userAccount.setDefaultEmail(unconfirmedEmail.getEmail());
        userAccount.setEnabled(true);
        userRepository.save(userAccount);
        return tokenProvider.generateToken(userAccount.getUsername());
    }
}
