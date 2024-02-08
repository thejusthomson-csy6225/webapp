package com.csye6225.controller;

import com.csye6225.model.User;
import com.csye6225.model.UserResponseDTO;
import com.csye6225.security.SecurityHandler;
import com.csye6225.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    private final SecurityHandler securityHandler;
    HttpHeaders headers = new HttpHeaders();

    public UserController(UserService userService, SecurityHandler securityHandler) {
        this.userService = userService;
        this.securityHandler = securityHandler;
        headers.setPragma("no-cache");
        headers.set("X-Content-Type-Options","nosniff");
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
    }

    @GetMapping("/self")
    public ResponseEntity<Object> getUserDetails(@RequestHeader("Authorization") String auth) {
        if (securityHandler.isValidUser(auth)) {
            UserResponseDTO user = userService.getUserDetailsAsDTO(securityHandler.returnUsername(auth));
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(user);
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping()
    public ResponseEntity<Object> insertUserDetails(@RequestHeader(value = "Authorization",required = false)
                                                        String auth, @RequestBody User user) {
        try {
            if(auth != null && !auth.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .headers(headers)
                        .body("Authorization is invalid");
            }
                UserResponseDTO savedUser = userService.insertUserDetails(user);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .headers(headers)
                        .body(savedUser);
        } catch (BadRequestException be) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body(be.getMessage());
        }

    }

    @PutMapping("/self")
    public ResponseEntity<Object> updateUserDetails(@RequestHeader("Authorization") String auth,@RequestBody User updateUser) {
        if (securityHandler.isValidUser(auth)) {
            if(updateUser!=null && updateUser.getUsername()==null && updateUser.getAccountCreated() == null && updateUser.getAccountUpdated() == null) {
                updateUser.setUsername(securityHandler.returnUsername(auth));
                userService.updateUserDetails(updateUser);
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .headers(headers)
                        .build();
            }
            else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Invalid request");
            }
        }
        else
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
    }

    @RequestMapping(method = {RequestMethod.HEAD, RequestMethod.OPTIONS}, path = {"", "/self"})
    public ResponseEntity<Object> handleHeadAndOptionsMethods(@RequestHeader(required = false,value = "Authorization")String auth) {
        if(auth != null && !auth.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body("Authorization is invalid");
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(headers)
                .build();
    }
}
