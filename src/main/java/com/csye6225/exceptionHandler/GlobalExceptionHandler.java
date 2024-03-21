package com.csye6225.exceptionHandler;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    HttpHeaders headers = new HttpHeaders();
    final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
        headers.setPragma("no-cache");
        headers.set("X-Content-Type-Options","nosniff");
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupportedException() {
        System.out.println("Inside handleMethodNotSupportedException");
        logger.error("HttpRequestMethodNotSupportedException occurred");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(headers)
                .build();
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException() {
        System.out.println("Inside handleResourceNotFoundException");
        logger.error("NoResourceFoundException occurred");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(headers)
                .build();
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException() {
        logger.error("BadRequestException occurred");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .build();
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException() {
        logger.error("UsernameNotFoundException occurred");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .headers(headers)
                .build();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions() {
        logger.error("Exception occurred");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .build();
    }

}
