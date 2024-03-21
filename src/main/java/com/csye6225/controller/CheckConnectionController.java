package com.csye6225.controller;

import com.csye6225.service.CheckConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/healthz")
public class CheckConnectionController {

    CheckConnectionService checkConnectionService;

    final Logger logger = LoggerFactory.getLogger(CheckConnectionController.class);

    @Autowired
    public CheckConnectionController(CheckConnectionService checkConnectionService) {
        this.checkConnectionService = checkConnectionService;
    }

    @RequestMapping(method = {RequestMethod.HEAD, RequestMethod.OPTIONS})
    public ResponseEntity<Object> handleHeadAndOptionsMethods(@RequestHeader(required = false,value = "Authorization")String auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
        if(auth != null && !auth.isEmpty()) {
            logger.warn("Authorization is invalid");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .body("Authorization is invalid");
        }
        logger.error("Method not allowed!");
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(headers)
                .build();
    }

    @GetMapping
    public ResponseEntity<String> checkConnection(@RequestBody(required = false) String body, @RequestParam(required = false) Map<String,String> params, @RequestHeader(required = false,value = "Authorization")String auth) {
        HttpHeaders headers = new HttpHeaders();
        logger.info("Logger is working");
        headers.set("Pragma", "no-cache");
        headers.set("X-Content-Type-Options", "nosniff");
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        try {
            if(auth != null && !auth.isEmpty()) {
                logger.warn("Authorization is invalid");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Authorization is invalid");
            }
            if ((params != null && !params.isEmpty()) || (body != null && !body.isEmpty())) {
                logger.warn("Param is empty/body is null");
                return ResponseEntity
                        .badRequest()
                        .headers(headers)
                        .build();
            }
            checkConnectionService.checkConnection();
            logger.info("Connection Successful");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .build();
        } catch (RuntimeException re) {
            logger.error("An error has occured");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .headers(headers)
                    .build();
        }
    }

}
