package com.call.my.owner.controllers;

import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.NoLoggedInUserException;
import com.call.my.owner.services.AutenticationService;
import com.call.my.owner.services.MessageService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final AutenticationService autenticationService;

    public MessageController(MessageService messageService, AutenticationService autenticationService) {
        this.messageService = messageService;
        this.autenticationService = autenticationService;
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity getMessages(@RequestParam String stuffId, @RequestParam int offset, @RequestParam int size, @RequestParam String direction)
            throws NoLoggedInUserException {
        UserAccount userAccount = autenticationService.getUser();
        try {
            return ok(messageService.getMessagesByUserAndStuff(userAccount.getId(), stuffId, offset, size, direction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity updateShownMessages(@RequestBody List<String> shownMessagesIds){
        try {
            messageService.updateMessagesAsShown(shownMessagesIds);
            return ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/user/count")
    public ResponseEntity getMessagesCount() throws NoLoggedInUserException {
        UserAccount userAccount = autenticationService.getUser();
        try {
            return ok(messageService.countMessagesByUser(userAccount.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/exists/new")
    public ResponseEntity newMessagesExistsByUSerAndStuff(@RequestParam String stuffId) throws NoLoggedInUserException {
        UserAccount userAccount = autenticationService.getUser();
        try {
            return ok(messageService.newMessagesExist(new ObjectId(stuffId), userAccount.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}