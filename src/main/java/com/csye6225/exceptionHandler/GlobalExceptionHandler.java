package com.csye6225.exceptionHandler;

import org.springframework.http.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.ServiceUnavailableException;

@ControllerAdvice
public class GlobalExceptionHandler {
    HttpHeaders headers = new HttpHeaders();
    public GlobalExceptionHandler() {
        headers.setPragma("no-cache");
        headers.set("X-Content-Type-Options","nosniff");
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupportedException() {
        System.out.println("Inside handleMethodNotSupportedException");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(headers)
                .build();
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException() {
        System.out.println("Inside handleResourceNotFoundException");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(headers)
                .build();
    }

}
