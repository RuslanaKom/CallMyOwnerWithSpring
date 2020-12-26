package com.call.my.owner.controllers;

import com.call.my.owner.entities.Stuff;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.NoStuffFoundException;
import com.call.my.owner.exceptions.UserNotFoundException;
import com.call.my.owner.services.MessageService;
import com.call.my.owner.services.SpringMailSender;
import com.call.my.owner.services.StuffService;
import com.call.my.owner.services.UserAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/contact")
public class ContactOwnerController {

    private final StuffService stuffService;
    private final UserAccountService userService;
    private final MessageService messageService;
    private final SpringMailSender springMailSender;
    private final String frontUrl;

    public ContactOwnerController(StuffService stuffService, UserAccountService userService, MessageService messageService,
                                  SpringMailSender springMailSender, @Value("${app.front.url}") String frontUrl) {
        this.stuffService = stuffService;
        this.userService = userService;
        this.messageService = messageService;
        this.springMailSender = springMailSender;
        this.frontUrl = frontUrl;
    }

    @GetMapping("/{id}")
    public RedirectView openContactForm(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            stuffService.getStuffById(id);
            return new RedirectView(frontUrl + "/contact/" + id);
        } catch (Exception e) {
            return new RedirectView(frontUrl + "/contact/0");
        }
    }

    @PostMapping("/sendmessage")
    public @ResponseBody
    ResponseEntity contactOwner(@RequestParam String stuffId, @RequestBody String message) {
        try {
            Stuff stuff = stuffService.getStuffById(stuffId);
            UserAccount userAccount = userService.getUserById(stuff.getUserId()
                    .toString());
            messageService.saveMessage(message, stuff.getId(), stuff.getStuffName(), userAccount.getId());
            springMailSender.sendMessage(userAccount.getDefaultEmail(), stuff.getStuffName(), message);
            return ok().build();
        } catch (NoStuffFoundException | UserNotFoundException e){
            return badRequest().body(e.getMessage());
        }
    }


}
