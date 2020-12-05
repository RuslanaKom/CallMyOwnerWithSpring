package com.call.my.owner.controllers;

import com.call.my.owner.dto.StuffDto;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.NoLoggedInUserException;
import com.call.my.owner.services.AutenticationService;
import com.call.my.owner.services.StuffService;
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
@RequestMapping("/stuff")
public class StuffController {

    private final StuffService stuffService;
    private final AutenticationService autenticationService;

    public StuffController(StuffService stuffService, AutenticationService autenticationService) {
        this.stuffService = stuffService;
        this.autenticationService = autenticationService;
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity createUpdateStuff(@RequestBody StuffDto stuffDto) throws NoLoggedInUserException {
        UserAccount userAccount = autenticationService.getUser();
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(stuffService.createUpdateStuff(userAccount, stuffDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping
    public @ResponseBody
    ResponseEntity deleteStuff(@RequestParam String stuffId) throws NoLoggedInUserException {
        System.out.println("removing item with id" + stuffId);
        UserAccount userAccount = autenticationService.getUser();
        try {
            stuffService.deleteStuffById(stuffId);
            return ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/id/defaultMessage")
    public @ResponseBody
    ResponseEntity getStuffDefaultMessage(@RequestParam String stuffId) {
        try {
            return ok(stuffService.getStuffById(stuffId).getDefaultMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity getStuffByUser(@RequestParam int offset,
                                  @RequestParam int size,
                                  @RequestParam String direction) throws NoLoggedInUserException {
        UserAccount userAccount = autenticationService.getUser();
        try {
            return ok(stuffService.getStuffByUser(userAccount, offset, size, direction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/id")
    public @ResponseBody
    ResponseEntity getStuffById(@RequestParam String stuffId) {
        try {
            UserAccount userAccount = autenticationService.getUser();
            return ok(stuffService.getStuffById(userAccount, stuffId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/qr")
    public @ResponseBody
    ResponseEntity createQrForStuff(Principal principal, @RequestBody String stuffId) {
        try {
            return ok(stuffService.createQrForStuff(principal, stuffId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/qr")
    public @ResponseBody
    ResponseEntity generateQr(@RequestParam String stuffId) {
        System.out.println("QR generation");
        try {
            UserAccount userAccount = autenticationService.getUser();
            return ResponseEntity.status(HttpStatus.OK).body(stuffService.generateQr(userAccount, stuffId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity countStuffByUser(){
        try {
            UserAccount userAccount = autenticationService.getUser();
            return ok(stuffService.countStuffByUser(userAccount.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
