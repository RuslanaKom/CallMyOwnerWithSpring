package com.call.my.owner.services;

import com.call.my.owner.controllers.UserController;
import com.call.my.owner.dao.UserDao;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.DuplicateUserNameException;
import com.call.my.owner.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserDao userDao;

    public UserAccountService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("searching for user by username");
        List<UserAccount> users = userDao.findByUsername(username);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return users.get(0);
    }

    public
    UserAccount createUserAccount(UserAccount userAccount) throws DuplicateUserNameException {
        try {
            this.loadUserByUsername(userAccount.getUsername());
        }
        catch (UsernameNotFoundException e) {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            userAccount.setPassword(encoder.encode(userAccount.getPassword()));
            logger.info("Creating new user account for user {}", userAccount.getUsername());
            return userDao.save(userAccount);
        }
        throw new DuplicateUserNameException();
    }

    public UserAccount getUserById(String id) throws UserNotFoundException {
        logger.info("Searching for user with id {}", id);
        return  userDao.findById(id).orElseThrow(()->new UserNotFoundException());
    }
}
