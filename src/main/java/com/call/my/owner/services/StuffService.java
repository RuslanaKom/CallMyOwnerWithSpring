package com.call.my.owner.services;

import com.call.my.owner.controllers.StuffController;
import com.call.my.owner.dao.StuffDao;
import com.call.my.owner.entities.Stuff;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.DuplicateStuffNameException;
import com.call.my.owner.exceptions.NoLoggedInUserException;
import com.call.my.owner.exceptions.NoStuffFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StuffService {

    private static final Logger logger = LoggerFactory.getLogger(StuffService.class);

    private static final String DEFAULT_EMAIL_IND = "default";

    private final StuffDao stuffDao;
    private final UserAccountService userAccountService;
    private final QrWriter qrWriter;

    public StuffService(StuffDao stuffDao, UserAccountService userAccountService, QrWriter qrWriter) {
        this.stuffDao = stuffDao;
        this.userAccountService = userAccountService;
        this.qrWriter = qrWriter;
    }

    public Stuff createUpdateStuff(Principal principal, Stuff stuff) throws Exception {
        if (principal == null) {
            throw new NoLoggedInUserException();
        }
        String userName = principal.getName();
        UserAccount userAccount = userAccountService.loadUserByUsername(userName);
        //check if stuff already in db
        if (stuff.getId() != null) {
            findStuffByIdAndUser(String.valueOf(stuff.getId()), userAccount);
        }
        //check if stuff with same name does not exist
        else {
            List<String> namesList = stuffDao.findStuffNamesByUserId(userAccount.getId())
                    .stream()
                    .map(Stuff::getStuffName)
                    .collect(Collectors.toList());
            if (namesList.contains(stuff.getStuffName())) {
                logger.info("Stuff with such name already exists");
                throw new DuplicateStuffNameException();
            }
        }
        stuff.setUserId(userAccount.getId());
        if (StringUtils.equals(stuff.getContactEmail(), DEFAULT_EMAIL_IND)) {
            stuff.setContactEmail(userAccount.getDefaultEmail());
        }
        logger.info("Adding some more stuff to user");
        return stuffDao.save(stuff);
    }

    public List<Stuff> getStuffByUser(Principal principal) throws NoLoggedInUserException {
        if (principal == null) {
            throw new NoLoggedInUserException();
        }
        UserAccount userAccount = userAccountService.loadUserByUsername(principal.getName());
        logger.info("Searching for user's stuff");
        return stuffDao.findByUserId(userAccount.getId());
    }

    public Stuff getStuffById(Principal principal, String id) throws NoLoggedInUserException, NoStuffFoundException {
        if (principal == null) {
            throw new NoLoggedInUserException();
        }
        UserAccount userAccount = userAccountService.loadUserByUsername(principal.getName());
        Stuff stuff = findStuffByIdAndUser(id, userAccount);
        return stuff;
    }

    public File createQrForStuff(Principal principal, String stuffId) throws NoLoggedInUserException, NoStuffFoundException {
        if (principal == null) {
            throw new NoLoggedInUserException();
        }
        UserAccount userAccount = userAccountService.loadUserByUsername(principal.getName());
        Stuff stuff = findStuffByIdAndUser(stuffId, userAccount);
        return qrWriter.createStuffQr(stuff);
    }

    private Stuff findStuffByIdAndUser(String stuffId, UserAccount userAccount) throws NoStuffFoundException {
        Stuff stuff = stuffDao.findById(stuffId)
                .orElseThrow(() -> new NoStuffFoundException("No stuff with this id found"));
        if (!stuff.getUserId()
                .equals(userAccount.getId())) {
            throw new NoStuffFoundException("No stuff with this id found for logged user");
        }
        return stuff;
    }

}
