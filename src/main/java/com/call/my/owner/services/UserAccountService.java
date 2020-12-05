package com.call.my.owner.services;

import com.call.my.owner.controllers.UserController;
import com.call.my.owner.dao.UserDao;
import com.call.my.owner.dto.UserAccountDto;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.DuplicateUserNameException;
import com.call.my.owner.exceptions.UserNotFoundException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserAccountService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserDao userDao;
    private final SpringMailSender springMailSender;

    public UserAccountService(UserDao userDao, SpringMailSender springMailSender) {
        this.userDao = userDao;
        this.springMailSender = springMailSender;
    }

    @Override
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("searching for user by username");
        return Optional.ofNullable(userDao.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserAccount createUserAccount(UserAccount userAccount, boolean isEmailConfirmed) throws DuplicateUserNameException {
        try {
            this.loadUserByUsername(userAccount.getUsername());
        } catch (UsernameNotFoundException e) {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            userAccount.setPassword(encoder.encode(userAccount.getPassword()));
            logger.info("Creating new user account for user {}", userAccount.getUsername());
            if (isEmailConfirmed) {
                userAccount.setEnabled(true);
            }
            UserAccount savedUser = userDao.save(userAccount);
            if (!isEmailConfirmed) {
                sendConfirmationEmail(savedUser);
            }
            return savedUser;
        }
        throw new DuplicateUserNameException();
    }

    public UserAccount getUserById(String id) throws UserNotFoundException {
        logger.info("Searching for user with id {}", id);
        return userDao.findById(new ObjectId(id))
                .orElseThrow(() -> new UserNotFoundException());
    }

    public UserAccount loadUserByEmail(String email) {
        return userDao.findByDefaultEmail(email);
    }

    private void sendConfirmationEmail(UserAccount savedUser) {
        String fullMessage = "Please confirm you email by clicking the link http://localhost:4200/confirm/" + savedUser.getId();
        springMailSender.sendMessage(savedUser.getDefaultEmail(), "Confirm you registration", fullMessage);
    }

    public void saveUser(UserAccount userAccount) {
        userDao.save(userAccount);
    }
}
