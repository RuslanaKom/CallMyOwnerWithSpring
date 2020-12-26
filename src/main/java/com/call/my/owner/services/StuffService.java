package com.call.my.owner.services;

import com.call.my.owner.dto.StuffDto;
import com.call.my.owner.entities.Stuff;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.DuplicateStuffNameException;
import com.call.my.owner.exceptions.NoStuffFoundException;
import com.call.my.owner.repository.MessageRepository;
import com.call.my.owner.repository.StuffRepository;
import com.call.my.owner.utils.CapitalLetterFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StuffService {

    private static final Logger logger = LoggerFactory.getLogger(StuffService.class);

    private final StuffRepository stuffRepository;
    private final MessageRepository messageRepository;
    private final QrPdfGenerator qrPdfGenerator;

    public StuffService(StuffRepository stuffRepository, MessageRepository messageRepository,
                        QrPdfGenerator qrPdfGenerator) {
        this.stuffRepository = stuffRepository;
        this.messageRepository = messageRepository;
        this.qrPdfGenerator = qrPdfGenerator;
    }

    public Stuff createUpdateStuff(UserAccount userAccount, StuffDto stuffDto) throws Exception {
        Stuff stuff = StuffDto.fromDto(stuffDto);
        boolean isNameChanged = false;
        if (stuffDto.getId() != null) {
            ObjectId stuffId = new ObjectId(stuffDto.getId());
            validateExistsByIdAndUserId(stuffId, userAccount.getId());
            isNameChanged = checkIfNameWasChanged(stuffDto, stuffId);
        }
        if (stuffDto.getId() == null || isNameChanged) {
            validateNoDuplicateName(stuff.getStuffName(), userAccount.getId());
        }
        stuff.setUserId(userAccount.getId());
        logger.info("Adding some more stuff to user.");
        return stuffRepository.save(stuff);
    }

    private boolean checkIfNameWasChanged(StuffDto stuffDto, ObjectId stuffId) throws NoStuffFoundException {
        Stuff existingStuff = stuffRepository.findById(stuffId)
                .orElseThrow(() -> new NoStuffFoundException("No stuff found by id."));
        return !StringUtils.equals(existingStuff.getStuffName(), stuffDto.getStuffName());
    }

    private void validateExistsByIdAndUserId(ObjectId stuffId, ObjectId userId) throws NoStuffFoundException {
        if (!stuffRepository.existsByIdAndUserId(stuffId, userId)) {
            throw new NoStuffFoundException("No stuff with this id found for logged user.");
        }
    }

    private void validateNoDuplicateName(String stuffName, ObjectId userId) throws DuplicateStuffNameException {
        if (stuffRepository.existsByUserIdAndStuffName(userId, stuffName)) {
            logger.info("Stuff with this name already exists.");
            throw new DuplicateStuffNameException();
        }
    }

    public Page<StuffDto> getStuffByUser(UserAccount userAccount,
                                         int offset, int size,
                                         String direction) {
        PageRequest request = PageRequest.of(offset, size,
                Sort.by(Sort.Direction.valueOf(direction), "stuffName"));
        return stuffRepository.findByUserId(userAccount.getId(), request)
                .map(StuffDto::toDto);
    }

    public StuffDto getStuffById(UserAccount userAccount, String id) throws NoStuffFoundException {
        Stuff stuff = findStuffByIdAndUser(new ObjectId(id), userAccount);
        return StuffDto.toDto(stuff);
    }

    private Stuff findStuffByIdAndUser(ObjectId stuffId, UserAccount userAccount) throws NoStuffFoundException {
        Stuff stuff = stuffRepository.findById(stuffId)
                .orElseThrow(() -> new NoStuffFoundException("No stuff with this id found."));
        if (!stuff.getUserId()
                .equals(userAccount.getId())) {
            throw new NoStuffFoundException("No stuff with this id found for logged user.");
        }
        return stuff;
    }

    public Stuff getStuffById(String stuffId) throws NoStuffFoundException {
        return stuffRepository.findById(new ObjectId(stuffId))
                .orElseThrow(() -> new NoStuffFoundException(
                        "This stuff either was deleted by owner or never existed."));
    }

    public byte[] generateQr(UserAccount userAccount, String stuffId, String size)
            throws Exception {
        Stuff stuff = findStuffByIdAndUser(new ObjectId(stuffId), userAccount);
        return qrPdfGenerator.generatePdf(stuff, size);
    }

    public void deleteStuffByIdAndUser(ObjectId userId, String stuffId) {
        ObjectId stuffIdObject = new ObjectId(stuffId);
        stuffRepository.deleteByIdAndUserId(stuffIdObject, userId);
        messageRepository.deleteByStuffId(stuffIdObject);
    }

    public Page<StuffDto> getStuffByUserAndName(UserAccount userAccount,
                                                int offset, int size,
                                                String direction,
                                                String stuffName) {
        PageRequest request = PageRequest.of(offset, size,
                Sort.by(Sort.Direction.valueOf(direction), "stuffName"));
        String formattedName = CapitalLetterFormatUtils.formatText(stuffName);
        return stuffRepository.
                findByUserIdAndStuffNameStartingWith(userAccount.getId(), formattedName, request)
                .map(StuffDto::toDto);
    }

    public Stuff getStuffFirst() {
        return stuffRepository.findAll().get(0);
    }
}
