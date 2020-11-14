package com.call.my.owner.services;

import com.call.my.owner.dao.StuffDao;
import com.call.my.owner.dto.StuffDto;
import com.call.my.owner.entities.Stuff;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.DuplicateStuffNameException;
import com.call.my.owner.exceptions.NoLoggedInUserException;
import com.call.my.owner.exceptions.NoStuffFoundException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StuffService {

    private static final Logger logger = LoggerFactory.getLogger(StuffService.class);

    private final StuffDao stuffDao;
    private final UserAccountService userAccountService;
    private final QrWriter qrWriter;

    public StuffService(StuffDao stuffDao, UserAccountService userAccountService, QrWriter qrWriter) {
        this.stuffDao = stuffDao;
        this.userAccountService = userAccountService;
        this.qrWriter = qrWriter;
    }

    public Stuff createUpdateStuff(UserAccount userAccount, StuffDto stuffDto) throws Exception {
        if (stuffDto.getId() != null) {
            validateExistsByIdAndUserId(new ObjectId(stuffDto.getId()), userAccount.getId());
        } else {
            validateNoDuplicateName(stuffDto.getStuffName(), userAccount.getId());
        }
        Stuff stuff = StuffDto.fromDto(stuffDto);
        stuff.setUserId(userAccount.getId());
        logger.info("Adding some more stuff to user");
        return stuffDao.save(stuff);
    }

    private void validateExistsByIdAndUserId(ObjectId stuffId, ObjectId userId) throws NoStuffFoundException {
        if(!stuffDao.existsByIdAndUserId(stuffId, userId)){
            throw new NoStuffFoundException("No stuff with this id found for logged user");
        }
    }

    private void validateNoDuplicateName(String stuffName, ObjectId userId) throws DuplicateStuffNameException {
        if (stuffDao.existsByUserIdAndStuffName(userId, stuffName)) {
            logger.info("Stuff with such name already exists");
            throw new DuplicateStuffNameException();
        }
    }
    public List<StuffDto> getStuffByUser(UserAccount userAccount) {
        List<Stuff> stuffList = stuffDao.findByUserId(userAccount.getId());
        return stuffList.stream()
                .map(s -> StuffDto.toDto(s))
                .collect(Collectors.toList());
    }

    public StuffDto getStuffById(UserAccount userAccount, String id) throws NoStuffFoundException {
        Stuff stuff = findStuffByIdAndUser(new ObjectId(id), userAccount);
        return StuffDto.toDto(stuff);
    }

    public File createQrForStuff(Principal principal, String stuffId) throws NoLoggedInUserException, NoStuffFoundException {
        if (principal == null) {
            throw new NoLoggedInUserException();
        }
        UserAccount userAccount = userAccountService.loadUserByUsername(principal.getName());
        Stuff stuff = findStuffByIdAndUser(new ObjectId(stuffId), userAccount);
        return qrWriter.createStuffQr(stuff);
    }

    private Stuff findStuffByIdAndUser(ObjectId stuffId, UserAccount userAccount) throws NoStuffFoundException {
        Stuff stuff = stuffDao.findById(stuffId)
                .orElseThrow(() -> new NoStuffFoundException("No stuff with this id found"));
        if (!stuff.getUserId()
                .equals(userAccount.getId())) {
            throw new NoStuffFoundException("No stuff with this id found for logged user");
        }
        return stuff;
    }

    public Stuff getStuffById(String stuffId) throws NoStuffFoundException {
        return stuffDao.findById(new ObjectId(stuffId))
                .orElseThrow(() -> new NoStuffFoundException("Nothing found"));
    }

    public File generateQr(UserAccount userAccount, String stuffId) throws NoStuffFoundException {
        Stuff stuff = findStuffByIdAndUser(new ObjectId(stuffId), userAccount);
        return qrWriter.createStuffQr(stuff);
    }

    public void deleteStuffById(String stuffId) {
        stuffDao.deleteById(new ObjectId(stuffId));
    }
}
