package com.csye6225.service;

import com.csye6225.model.User;
import com.csye6225.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserDetails(String id) {
        return userRepository.findById(id);
    }

    public User insertUserDetails(User user) {
        userRepository.save(user);
        return user;
    }

//    public User updateUserDetails(String id, User updateUser) {
//        Optional<User>
//    }
}
