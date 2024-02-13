package com.csye6225.service;

import com.csye6225.model.User;
import com.csye6225.model.UserResponseDTO;
import com.csye6225.repository.UserRepository;
import com.csye6225.security.SecurityHandler;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.coyote.BadRequestException;
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

    public UserResponseDTO insertUserDetails(User user) throws BadRequestException {
        boolean userExists = userRepository.findByUsername(user.getUsername()) != null;
        if(EmailValidator.getInstance().isValid(user.getUsername()) && !userExists) {
            if(null != user.getPassword() && !user.getPassword().isBlank()) {
                if(null != user.getFirstName() && !user.getFirstName().isBlank()
                        && null != user.getLastName() && !user.getLastName().isBlank()) {
                    SecurityHandler securityHandler = new SecurityHandler(userRepository);
                    user.setPassword(securityHandler.passwordEncoder(user.getPassword()));
                    User savedUser = userRepository.save(user);
                    ModelMapper mapper = new ModelMapper();
                    return mapper.map(savedUser, UserResponseDTO.class);
                }
                else {
                    throw new BadRequestException("Firstname and/or Lastname cannot be null or blank");
                }
            }
            else {
                throw new BadRequestException("Password cannot be null or blank");
            }
        }
        else {
            throw new BadRequestException("Username is not valid/User already exists. Has to be unique and a valid email id");
        }
    }

    public void updateUserDetails(User updateUser) {
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
        userRepository.save(existingUser);
    }
}
