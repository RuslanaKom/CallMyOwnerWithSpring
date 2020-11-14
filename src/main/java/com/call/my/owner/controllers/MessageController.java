package com.call.my.owner.controllers;

import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.NoLoggedInUserException;
import com.call.my.owner.services.AutenticationService;
import com.call.my.owner.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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
    ResponseEntity getMessages(@RequestParam String stuffId) throws NoLoggedInUserException {
        UserAccount userAccount = autenticationService.getUser();
        try {
            return ok(messageService.getMessagesByUserAndStuff(userAccount.getId(), stuffId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
