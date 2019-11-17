package com.call.my.owner.controllers;

import com.call.my.owner.entities.Stuff;
import com.call.my.owner.services.StuffService;
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

@RestController
@RequestMapping("/stuff")
public class StuffController {

    private final StuffService stuffService;

    public StuffController(StuffService stuffService) {
        this.stuffService = stuffService;
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<?> createUpdateStuff(Principal principal, @RequestBody Stuff stuff) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(stuffService.createUpdateStuff(principal, stuff));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<?> getStuffByUser(Principal principal) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(stuffService.getStuffByUser(principal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/id")
    public @ResponseBody
    ResponseEntity<?> getStuffById(Principal principal, @RequestParam String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(stuffService.getStuffById(principal, id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/qr")
    public @ResponseBody
    ResponseEntity<?> createQrForStuff(Principal principal, @RequestParam String stuffId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(stuffService.createQrForStuff(principal, stuffId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
