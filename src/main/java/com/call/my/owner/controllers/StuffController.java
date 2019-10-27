package com.call.my.owner.controllers;

import com.call.my.owner.dao.StuffDao;
import com.call.my.owner.entities.Stuff;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.NoLoggedInUserException;
import com.call.my.owner.exceptions.NoStuffFoundException;
import com.call.my.owner.exceptions.UserNotFoundException;
import com.call.my.owner.security.UserAccountService;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stuff")
public class StuffController {
    private static final Logger logger = LoggerFactory.getLogger(StuffController.class);
    private static final CharSequence DEFAULT_EMAIL_IND = "default";

    @Autowired
    private StuffDao stuffDao;
    @Autowired
    UserAccountService userAccountService;

    @PostMapping
    public @ResponseBody
    ResponseEntity<Stuff> createUpdateStuff(Principal principal, @RequestBody Stuff stuff) throws NoLoggedInUserException, NoStuffFoundException {
        if (principal == null) {
            throw new NoLoggedInUserException();
        }
        String userName = principal.getName();
        UserAccount userAccount = userAccountService.loadUserByUsername(userName);
        if (stuff.getId() != null) {
            Stuff existingStuff = stuffDao.findById(String.valueOf(stuff.getId())).orElseThrow(()-> new NoStuffFoundException("Stuff has id but cannot be found"));
            if (!existingStuff.getUserId().equals(userAccount.getId())) {
                throw new NoStuffFoundException("While trying to update no stuff with this id found for logged user");
            }
        }
        stuff.setUserId(userAccount.getId());
        if (StringUtils.equals(stuff.getContactEmail(), DEFAULT_EMAIL_IND)) {
            stuff.setContactEmail(userAccount.getDefaultEmail());
        }
        logger.info("Adding some more stuff to user");
        return new ResponseEntity<Stuff>(stuffDao.save(stuff), HttpStatus.CREATED);
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<Stuff>> getStuffByUserId(Principal principal) throws NoLoggedInUserException {
        if (principal == null) {
            throw new NoLoggedInUserException();
        }
        UserAccount userAccount = userAccountService.loadUserByUsername(principal.getName());
        logger.info("Searching for user's stuff");
        return new ResponseEntity<List<Stuff>>(stuffDao.findByUserId(userAccount.getId()), HttpStatus.OK);
    }

    @GetMapping("/id")
    public @ResponseBody
    ResponseEntity<Stuff> getStuffById(Principal principal, @RequestParam String id) throws NoLoggedInUserException, NoStuffFoundException {
        logger.info("Searching for user's stuff by id");
        if (principal == null) {
            throw new NoLoggedInUserException();
        }
        UserAccount userAccount = userAccountService.loadUserByUsername(principal.getName());
        Stuff stuff = stuffDao.findById(id).orElseThrow(() -> new NoStuffFoundException("No stuff with this id found"));
        if (!stuff.getUserId().equals(userAccount.getId())) {
            throw new NoStuffFoundException("No stuff with this id found for logged user");
        }
        return new ResponseEntity<Stuff>(stuff, HttpStatus.OK);
    }
}
