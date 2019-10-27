package com.call.my.owner.controllers;

import com.call.my.owner.dao.UserDao;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDao userDao;

    @PostMapping
    public @ResponseBody ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccount userAccount){
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        userAccount.setPassword(encoder.encode(userAccount.getPassword()));
        logger.info("Creating new user account for user {}", userAccount.getUsername());
        return new ResponseEntity<UserAccount>(userDao.save(userAccount), HttpStatus.CREATED);
    }

    @GetMapping
    public  @ResponseBody ResponseEntity<UserAccount> getUserById(@RequestParam String id) throws UserNotFoundException {
        logger.info("Searching for user with id {}", id);
        return  new ResponseEntity<UserAccount>(userDao.findById(id).orElseThrow(()->new UserNotFoundException()), HttpStatus.OK);
    }

    @GetMapping("/health")
    public @ResponseBody ResponseEntity<String> healthCheck(){
        logger.info("App health check");
        return new ResponseEntity<String>("Hello, you!", HttpStatus.OK);
    }


}
