package com.call.my.owner.controllers;

import com.call.my.owner.dto.UserAccountDto;
import com.call.my.owner.dto.UserLoginDto;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.UserNotFoundException;
import com.call.my.owner.security.JwtAuthenticationResponse;
import com.call.my.owner.security.JwtTokenProvider;
import com.call.my.owner.services.AutenticationService;
import com.call.my.owner.services.ConfirmationService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthenticationManager authenticationManager;
    private final UserAccountService userAccountService;
    private final JwtTokenProvider tokenProvider;
    private final AutenticationService autenticationService;
    private final ConfirmationService confirmationService;

    public UserController(AuthenticationManager authenticationManager, UserAccountService userAccountService,
                          JwtTokenProvider tokenProvider, AutenticationService autenticationService,
                          ConfirmationService confirmationService) {
        this.authenticationManager = authenticationManager;
        this.userAccountService = userAccountService;
        this.tokenProvider = tokenProvider;
        this.autenticationService = autenticationService;
        this.confirmationService = confirmationService;
    }

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@RequestBody UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(         // AUTHENTICATION
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(), userLoginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userAccount.getUsername());       // JWT TOKEN CREATION
        return ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity createUserAccount(@RequestBody UserAccountDto userAccountDto) {
        logger.info("Registering new user");
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userAccountService.createUserAccount(userAccountDto.toUserAccount(),
                            false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public @ResponseBody
    ResponseEntity<?> updateUserAccount(@RequestBody UserAccountDto userAccountDto) {
        try {
            UserAccount userAccount = autenticationService.getUser();
            return ok(userAccountService.updateUserAccount(userAccountDto, userAccount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity confirmRegistration(@RequestBody String unconfirmedEmailId) {
        try {
            String jwt = confirmationService.confirmEmail(unconfirmedEmailId);
            return ok(new JwtAuthenticationResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity getUserProfile() throws UserNotFoundException {
        try {
            UserAccount userAccount = autenticationService.getUser();
            return ok(UserAccountDto.toDto(userAccount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/health")
    public @ResponseBody
    ResponseEntity<String> healthCheck() {
        logger.info("App health check");
        return new ResponseEntity<String>("Hello, you!", HttpStatus.OK);
    }

}
