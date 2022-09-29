package com.bridgelabz.notesapp.utility.security;

import com.bridgelabz.notesapp.exception.CustomException;
import lombok.Data;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
@Data
public class UserCredentials implements UserDetailsService {
    private String userName;
    private String passWord;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Username" + username);
        System.out.println("UUUUsername" + userName);
        if (username.equals(userName)) {
            return new User(username, passWord, new ArrayList<>());
        } else {
            throw new CustomException("Invalid Credentials");
        }
    }
}
