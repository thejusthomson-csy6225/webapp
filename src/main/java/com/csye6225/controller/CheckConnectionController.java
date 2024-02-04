package com.csye6225.controller;

import com.csye6225.service.CheckConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/healthz")
public class CheckConnectionController {

    CheckConnectionService checkConnectionService;

    @Autowired
    public CheckConnectionController(CheckConnectionService checkConnectionService) {
        this.checkConnectionService = checkConnectionService;
    }

    @RequestMapping(method = {RequestMethod.HEAD, RequestMethod.OPTIONS})
    public ResponseEntity<Void> handleHeadAndOptionsMethods() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(headers)
                .build();
    }

    @GetMapping
    public ResponseEntity<String> checkConnection(@RequestBody(required = false) String body, @RequestParam(required = false) Map<String,String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Pragma", "no-cache");
        headers.set("X-Content-Type-Options", "nosniff");
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        try {
            if ((params != null && !params.isEmpty()) || (body != null && !body.isEmpty())) {
                return ResponseEntity
                        .badRequest()
                        .headers(headers)
                        .build();
            }
            checkConnectionService.checkConnection();
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .build();
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .headers(headers)
                    .build();
        }
    }

}
