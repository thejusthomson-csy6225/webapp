package com.csye6225.controller;

import com.csye6225.model.UserResponseDTO;
import com.csye6225.security.SecurityHandler;
import com.csye6225.model.User;
import com.csye6225.service.UserService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> getUserDetails(@RequestHeader("Authorization") String auth) {
        if (securityHandler.isValidUser(auth)) {
            UserResponseDTO user = userService.getUserDetailsAsDTO(securityHandler.returnUsername(auth));
            return ResponseEntity
                    .ok()
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .body(user);
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
                .cacheControl(CacheControl.noCache().mustRevalidate())
                .body(savedUser);
    }

    @PutMapping("/self")
    public ResponseEntity<Object> updateUserDetails(@RequestHeader("Authorization") String auth,@RequestBody User updateUser) {
        if (securityHandler.isValidUser(auth)) {
            updateUser.setUsername(securityHandler.returnUsername(auth));
            userService.updateUserDetails(updateUser);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .build();
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
