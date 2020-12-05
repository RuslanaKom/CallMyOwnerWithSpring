package com.call.my.owner.controllers;

import com.call.my.owner.dto.UserAccountDto;
import com.call.my.owner.dto.UserLoginDto;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.UserNotFoundException;
import com.call.my.owner.security.JwtAuthenticationResponse;
import com.call.my.owner.security.JwtTokenProvider;
import com.call.my.owner.services.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthenticationManager authenticationManager;
    private final UserAccountService userAccountService;
    private final JwtTokenProvider tokenProvider;

    public UserController(AuthenticationManager authenticationManager, UserAccountService userAccountService, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userAccountService = userAccountService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(         // AUTHENTICATION
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(), userLoginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userAccount.getUsername());                   // JWT TOKEN CREATION
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping
    public @ResponseBody ResponseEntity<?> createUserAccount(@RequestBody UserAccountDto userAccountDto){
        logger.info("registering user");
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userAccountService.createUserAccount(userAccountDto.toUserAccount(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/confirm")
   public ResponseEntity confirmRegistration(@RequestBody String userId) {
        try {
            UserAccount userAccount = userAccountService.getUserById(userId);
            userAccount.setEnabled(true);
            userAccountService.saveUser(userAccount);
            String jwt = tokenProvider.generateToken(userAccount.getUsername());
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        }
        catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public  @ResponseBody ResponseEntity<?> getUserById(@RequestParam String id) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userAccountService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/health")
    public @ResponseBody ResponseEntity<String> healthCheck(){
        logger.info("App health check");
        return new ResponseEntity<String>("Hello, you!", HttpStatus.OK);
    }

}
