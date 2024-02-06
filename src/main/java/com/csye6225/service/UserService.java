package com.csye6225.service;

import com.csye6225.model.User;
import com.csye6225.model.UserResponseDTO;
import com.csye6225.repository.UserRepository;
import com.csye6225.security.SecurityHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO getUserDetailsAsDTO(String username) {
        User user = userRepository.findByUsername(username);
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user, UserResponseDTO.class);
    }

    public User getUserDetails(String username) {
        return userRepository.findByUsername(username);
    }

    public User insertUserDetails(User user) {
        return userRepository.save(user);
    }

    public UserResponseDTO updateUserDetails(User updateUser) {
        User existingUser = userRepository.findByUsername(updateUser.getUsername());
        if(null != updateUser.getFirstName() && !updateUser.getFirstName().isBlank()) {
            existingUser.setFirstName(updateUser.getFirstName());
        }
        if(null != updateUser.getLastName() && !updateUser.getLastName().isBlank()) {
            existingUser.setLastName(updateUser.getLastName());
        }
        if(null != updateUser.getPassword() && !updateUser.getPassword().isBlank()) {
            SecurityHandler securityHandler = new SecurityHandler(userRepository);
            existingUser.setPassword(securityHandler.passwordEncoder(updateUser.getPassword()));
        }
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userRepository.save(existingUser), UserResponseDTO.class);
    }
}
