package com.call.my.owner.controllers;

import com.call.my.owner.dao.StuffDao;
import com.call.my.owner.entities.Stuff;
import com.call.my.owner.exceptions.NoStuffFoundException;
import com.call.my.owner.services.SpringMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactOwnerController {

    @Autowired
    private StuffDao stuffDao;

    @Autowired
    private SpringMailSender springMailSender;

    @GetMapping("/{id}")
    public RedirectView openContactForm(@PathVariable String id, RedirectAttributes redirectAttributes) throws NoStuffFoundException {
        Stuff stuff = stuffDao.findById(id).orElseThrow(() -> new NoStuffFoundException("Nothing found"));
  //      redirectAttributes.addAttribute("stuffId", id);
 //       return "redirect:/ui";
        return new RedirectView("http://localhost:3000/contact/ui/" + id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(id, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "localhost:9999/sendmessage/ui";
//        restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @GetMapping("/sendmessage")
    public @ResponseBody
    ResponseEntity<List<Stuff>> contactOwner(@RequestParam String id, @RequestParam String message) throws NoStuffFoundException {
        Stuff stuff = stuffDao.findById(id).orElseThrow(() -> new NoStuffFoundException("Nothing found"));
        String fullMessage = stuff.getDefaultMessage() + System.lineSeparator() + message;
        springMailSender.sendMessage("stuffost@gmail.com", stuff.getStuffName(), fullMessage);
        /* todo uncomment after testing */
        // springMailSender.sendMessage(stuff.getContactEmail(), stuff.getStuffName(), fullMessage);

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
