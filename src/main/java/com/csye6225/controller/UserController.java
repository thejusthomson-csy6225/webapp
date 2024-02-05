package com.csye6225.controller;

import com.csye6225.Security.SecurityHandler;
import com.csye6225.model.User;
import com.csye6225.service.UserService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    private final SecurityHandler securityHandler;

    public UserController(UserService userService, SecurityHandler securityHandler) {
        this.userService = userService;
        this.securityHandler = securityHandler;
    }

    @GetMapping("/self")
    public ResponseEntity<Object> getAllUserDetails(@RequestHeader("Authorization") String auth) {
        if (securityHandler.isValidUser(auth)) {
            Optional<User> user = userService.getUserDetails(securityHandler.returnUserDetails(auth));
            user.ifPresent(value -> value.setPassword(null));
            return ResponseEntity.ok().body(user);
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping()
    public ResponseEntity<Object> insertUserDetails(@RequestBody User user) {
        user.setPassword(securityHandler.passwordEncoder(user.getPassword()));
        User savedUser = userService.insertUserDetails(user);
        savedUser.setPassword(null);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .cacheControl(CacheControl.noCache())
                .body(savedUser);
    }

//    @GetMapping
//    public ResponseEntity<Object> updateUserDetails(@RequestParam Map<String, String> id, @RequestBody User updateUser) {
//
//    }
}
