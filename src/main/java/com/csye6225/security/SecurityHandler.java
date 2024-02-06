package com.csye6225.security;


import com.csye6225.model.User;
import com.csye6225.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SecurityHandler {

    private final UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public SecurityHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValidUser(String auth) {
        String base64String = auth.substring("Basic ".length());
        String[] parts = decodeBase64(base64String);
        String username = parts[0];
        String password = parts[1];
        User loggedUser = userRepository.findByUsername(username);
        return loggedUser != null
                && loggedUser.getUsername().equals(username)
                && bCryptPasswordEncoder.matches(password, loggedUser.getPassword());
    }

    public String returnUsername(String auth) {
        String base64String = auth.substring("Basic ".length());
        String[] parts = decodeBase64(base64String);
        return parts[0];
    }

    public String[] decodeBase64(String base64String) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
       String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        return decodedString.split(":");
    }

    public String passwordEncoder(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
