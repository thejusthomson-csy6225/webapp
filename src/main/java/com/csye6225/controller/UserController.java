package com.csye6225.controller;

import com.csye6225.model.User;
import com.csye6225.model.UserResponseDTO;
import com.csye6225.security.SecurityHandler;
import com.csye6225.service.PublisherService;
import com.csye6225.service.UserService;
import com.google.api.gax.rpc.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;
    private final SecurityHandler securityHandler;
    HttpHeaders headers = new HttpHeaders();
    final Logger logger = LoggerFactory.getLogger(CheckConnectionController.class);

    public UserController(UserService userService, SecurityHandler securityHandler) {
        this.userService = userService;
        this.securityHandler = securityHandler;
        headers.setPragma("no-cache");
        headers.set("X-Content-Type-Options","nosniff");
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
    }

    @GetMapping("/self")
    public ResponseEntity<Object> getUserDetails(@RequestHeader("Authorization") String auth, @RequestBody(required = false) String userReq) {
        if(userReq != null) {
            logger.warn("Request is null");
            logger.error("Bad Request");
            logger.debug("Request is: "+ userReq);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .build();
        }
        try {
            if (securityHandler.isValidUser(auth)) {
                UserResponseDTO user = userService.getUserDetailsAsDTO(securityHandler.returnUsername(auth));
                logger.warn("Auth is invalid");
                logger.error("Unauthorized!");
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .body(user);
            } else {
                logger.warn("Auth is invalid");
                logger.error("Unauthorized!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            if(e.getMessage().contains("Not Verified")) {
                logger.warn("Not verified!");
                logger.error(e.getMessage());
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .headers(headers)
                        .body(Collections.singletonMap("status: ",e.getMessage()));
            }
            logger.warn("Exception occurred");
            logger.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .headers(headers)
                    .body(Collections.singletonMap("status",e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<Object> insertUserDetails(@RequestHeader(value = "Authorization",required = false)
                                                        String auth, @RequestBody User user) {
        try {
            if(auth != null && !auth.isEmpty()) {
                logger.warn("Auth is invalid");
                logger.error("Unauthorized!");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .headers(headers)
                        .body(Collections.singletonMap("status","Authorization is invalid"));
            }
            UserResponseDTO savedUser = userService.insertUserDetails(user);
            logger.debug("Saved user is: "+savedUser);
            return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .headers(headers)
                        .body(savedUser);
        }
        catch (Exception be) {
            logger.warn("Exception occurred");
            logger.error(be.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body(Collections.singletonMap("status",be.getMessage()));
        }

    }

    @PutMapping("/self")
    public ResponseEntity<Object> updateUserDetails(@RequestHeader("Authorization") String auth,@RequestBody User updateUser) {
        try {
            if (securityHandler.isValidUser(auth)) {
                if (updateUser != null && updateUser.getUsername() == null && updateUser.getAccountCreated() == null && updateUser.getAccountUpdated() == null && updateUser.getVerificationLinkExpirationTime() == null && !updateUser.isVerified()) {
                    updateUser.setUsername(securityHandler.returnUsername(auth));
                    userService.updateUserDetails(updateUser);
                    logger.info("Update success!");
                    return ResponseEntity
                            .status(HttpStatus.NO_CONTENT)
                            .headers(headers)
                            .build();
                } else {
                    logger.warn("Invalid request");
                    logger.error("Request is wrong");
                    logger.debug("Bad request is: "+ updateUser);
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(Collections.singletonMap("status","Invalid request"));
                }
            } else
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
        }
        catch (Exception e) {
            if(e.getMessage().contains("Not Verified")) {
                logger.warn("Not verified!");
                logger.error(e.getMessage());
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .headers(headers)
                        .body(Collections.singletonMap("status: ",e.getMessage()));
            }
            logger.warn("Exception occurred!");
            logger.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body(Collections.singletonMap("status: ",e.getMessage()));
        }
    }

    @RequestMapping(method = {RequestMethod.HEAD, RequestMethod.OPTIONS}, path = {"", "/self"})
    public ResponseEntity<Object> handleHeadAndOptionsMethods(@RequestHeader(required = false,value = "Authorization")String auth) {
        if(auth != null && !auth.isEmpty()) {
            logger.warn("Authorization is invalid");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body(Collections.singletonMap("status","Authorization is invalid"));
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(headers)
                .build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verifyUser(@RequestParam(required = false) Map<String,String> params, @RequestBody(required = false) String body, @RequestHeader(required = false, value = "isIntegrationTest") boolean isIntegrationTest ) {
        if(null != body && !body.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .build();        }
        try {
            String message = userService.verifyUser(params, isIntegrationTest);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(Collections.singletonMap("verificationStatus", message));
        }
        catch (Exception e) {
            logger.warn("Verification failed!");
            logger.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body(Collections.singletonMap("status", e.getMessage()));
        }
    }
}
