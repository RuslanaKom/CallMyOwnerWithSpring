package com.call.my.owner.services;

import com.call.my.owner.controllers.UserController;
import com.call.my.owner.repository.UserRepository;
import com.call.my.owner.dto.UserAccountDto;
import com.call.my.owner.dto.UserAccountWithTokenDto;
import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.exceptions.DuplicateUserNameException;
import com.call.my.owner.exceptions.UserNotFoundException;
import com.call.my.owner.security.JwtAuthenticationResponse;
import com.call.my.owner.security.JwtTokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final ConfirmationService confirmationService;
    private final JwtTokenProvider tokenProvider;

    public UserAccountService(UserRepository userRepository, ConfirmationService confirmationService, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.confirmationService = confirmationService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserAccount createUserAccount(UserAccount userAccount, boolean isEmailConfirmed)
            throws DuplicateUserNameException {
        if (existsByUsername(userAccount.getUsername())) {
            throw new DuplicateUserNameException();
        }
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        userAccount.setPassword(encoder.encode(userAccount.getPassword()));
        if (isEmailConfirmed) {
            userAccount.setEnabled(true);
        }
        UserAccount savedUser = userRepository.save(userAccount);
        if (!isEmailConfirmed) {
            confirmationService.sendConfirmationEmail(savedUser.getId(), savedUser.getDefaultEmail());
        }
        return savedUser;
    }

    public UserAccount getUserById(String id) throws UserNotFoundException {
        logger.info("Searching for user with id {}", id);
        return userRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new UserNotFoundException());
    }

    public UserAccount loadUserByEmail(String email) {
        return userRepository.findByDefaultEmail(email);
    }

    public void saveUser(UserAccount userAccount) {
        userRepository.save(userAccount);
    }

    public UserAccountWithTokenDto updateUserAccount(UserAccountDto userAccountDto, UserAccount userAccount) {
        String currentEmail = userAccount.getDefaultEmail();
        if (!StringUtils.equals(userAccount.getUsername(), userAccountDto.getUsername())
                && userRepository.existsByUsernameAndIdNot(userAccountDto.getUsername(), userAccount.getId())){
                throw new DuplicateUserNameException();
        }
        userAccount.setUsername(userAccountDto.getUsername());
        if (!StringUtils.equalsIgnoreCase(currentEmail, userAccountDto.getDefaultEmail())) {
            confirmationService.sendConfirmationEmail(userAccount.getId(), userAccountDto.getDefaultEmail());
        }
        userRepository.save(userAccount);
        UserAccountWithTokenDto userAccountWithTokenDto = new UserAccountWithTokenDto();
        userAccountWithTokenDto.setAccessToken(new JwtAuthenticationResponse(tokenProvider.generateToken(userAccount.getUsername())));
        userAccountWithTokenDto.setUserAccountDto(UserAccountDto.toDto(userAccount));
        return userAccountWithTokenDto;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
