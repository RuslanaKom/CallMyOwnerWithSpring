package com.call.my.owner.security;

import com.call.my.owner.dao.UserDao;
import com.call.my.owner.entities.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    @Override
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("searching for user by username");
        return Optional.ofNullable(userDao.findByUsername(username).get(0)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
