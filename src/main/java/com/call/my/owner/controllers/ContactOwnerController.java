package com.call.my.owner.controllers;

import com.call.my.owner.dao.StuffDao;
import com.call.my.owner.entities.Stuff;
import com.call.my.owner.exceptions.NoStuffFoundException;
import com.call.my.owner.services.SpringMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactOwnerController {

    @Autowired
    private StuffDao stuffDao;

    @Autowired
    private SpringMailSender springMailSender;

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<List<Stuff>> contactOwner(@RequestParam String id) throws NoStuffFoundException {
        Stuff stuff = stuffDao.findById(id).orElseThrow(() -> new NoStuffFoundException("Nothing found"));
        springMailSender.sendMessage("stuffost@gmail.com","Your stuff wants to talk to you", stuff.getDefaultMessage());
        /* todo uncomment after testing */
       // springMailSender.sendMessage(stuff.getContactEmail(),"Your stuff wants to talk to you", stuff.getDefaultMessage());

       return new ResponseEntity<>(HttpStatus.OK);
    }

}
