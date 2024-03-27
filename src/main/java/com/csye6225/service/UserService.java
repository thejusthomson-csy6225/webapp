package com.csye6225.service;

import com.csye6225.model.User;
import com.csye6225.model.UserResponseDTO;
import com.csye6225.repository.UserRepository;
import com.csye6225.security.SecurityHandler;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO getUserDetailsAsDTO(String username) throws Exception{
        User user = userRepository.findByUsername(username);
        if(!user.isVerified()) {
            throw new Exception("User Not Verified, please verify");
        }
        ModelMapper mapper = new ModelMapper();
        logger.debug("Data returned from DB: "+user);
        return mapper.map(user, UserResponseDTO.class);
    }

    public User getUserDetails(String username) {
        return userRepository.findByUsername(username);
    }

    public UserResponseDTO insertUserDetails(User user) throws BadRequestException {
        boolean userExists = userRepository.findByUsername(user.getUsername()) != null;
        logger.debug("User Exists? "+userExists);
        if(EmailValidator.getInstance().isValid(user.getUsername()) && !userExists) {
            if(null != user.getPassword() && !user.getPassword().isBlank()) {
                if(isValidPostRequest(user)) {
                    SecurityHandler securityHandler = new SecurityHandler(userRepository);
                    user.setPassword(securityHandler.passwordEncoder(user.getPassword()));
                    User savedUser = userRepository.save(user);
                    ModelMapper mapper = new ModelMapper();
                    return mapper.map(savedUser, UserResponseDTO.class);
                }
                else {
                    throw new BadRequestException("Invalid request");
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

    public void updateUserDetails(User updateUser) throws Exception {
        logger.debug("Request to be updated: "+updateUser);
        if (isNotValidPutRequest(updateUser)) {
            throw new BadRequestException("Body is blank/ fields are empty");
        }
        User existingUser = userRepository.findByUsername(updateUser.getUsername());
        if(!existingUser.isVerified()) {
            throw new Exception("User Not Verified, please verify");
        }
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
        logger.debug("Data to be inserted: "+existingUser);
        userRepository.save(existingUser);
    }

    public boolean isValidPostRequest(User user) {
        return null != user.getFirstName() && !user.getFirstName().isBlank()
                && null != user.getLastName() && !user.getLastName().isBlank()
                && user.getId() == null && user.getAccountCreated() == null
                && user.getAccountUpdated() == null
                && user.getVerificationMailSentTime() == null
                && !user.isVerified();
    }

    public boolean isNotValidPutRequest(User user) {
        if (user.getFirstName() == null
                && user.getLastName() == null
                && user.getPassword() == null) {
            return true;
        }
        return ((null != user.getFirstName() && user.getFirstName().isBlank()) || (null != user.getLastName() && user.getLastName().isBlank()) || (null != user.getPassword() && user.getPassword().isBlank()));
    }

    public String verifyUser(Map<String,String> params) {
        String username = params.get("username");
        String token = params.get("token");
        User user = getUserDetails(username);
        if(user.isVerified()) {
            return username + " already verified!";
        }
        Instant instantVerificationTime = user.getVerificationMailSentTime().toInstant();
        System.out.println("instant time: "+Instant.now());
        System.out.println("db time: "+instantVerificationTime);
        Duration duration = Duration.between(instantVerificationTime, Instant.now());
        if(token.equals(user.getId()) && duration.toSeconds() < 60 ) {
            user.setVerified(true);
            userRepository.save(user);
            return username + " verified successfully!";
        }
        user.setVerified(false);
        userRepository.save(user);
        return "Sorry, verification link is incorrect or Expired!";
    }
}
