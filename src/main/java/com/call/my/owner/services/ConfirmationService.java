package com.call.my.owner.services;

import com.call.my.owner.dao.UnconfirmedEmailDao;
import com.call.my.owner.dao.UserDao;
import com.call.my.owner.entities.UnconfirmedEmail;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.ConfirmationNotFoundException;
import com.call.my.owner.exceptions.UserNotFoundException;
import com.call.my.owner.security.JwtTokenProvider;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationService {

    private final UnconfirmedEmailDao unconfirmedEmailDao;
    private final UserDao userDao;
    private final SpringMailSender springMailSender;
    private final JwtTokenProvider tokenProvider;

    public ConfirmationService(UnconfirmedEmailDao unconfirmedEmailDao, UserDao userDao, SpringMailSender springMailSender, JwtTokenProvider tokenProvider) {
        this.unconfirmedEmailDao = unconfirmedEmailDao;
        this.userDao = userDao;
        this.springMailSender = springMailSender;
        this.tokenProvider = tokenProvider;
    }


    public void sendConfirmationEmail(ObjectId userId, String email) {
        UnconfirmedEmail unconfirmedEmail = new UnconfirmedEmail();
        unconfirmedEmail.setUserId(userId);
        unconfirmedEmail.setEmail(email);
        unconfirmedEmail = unconfirmedEmailDao.save(unconfirmedEmail);
        String fullMessage = "Please confirm you email by clicking the link http://localhost:4200/confirm/" + unconfirmedEmail.getId();
        springMailSender.sendMessage(email, "Confirm you registration", fullMessage);
    }

    public String confirmEmail(String unconfirmedEmailId) throws ConfirmationNotFoundException, UserNotFoundException {
        UnconfirmedEmail unconfirmedEmail = unconfirmedEmailDao.findById(new ObjectId(unconfirmedEmailId))
                .orElseThrow(() -> new ConfirmationNotFoundException());
        UserAccount userAccount = userDao.findById(unconfirmedEmail.getUserId())
                .orElseThrow(() -> new UserNotFoundException());
        userAccount.setDefaultEmail(unconfirmedEmail.getEmail());
        userAccount.setEnabled(true);
        userDao.save(userAccount);
        return tokenProvider.generateToken(userAccount.getUsername());
    }
}
