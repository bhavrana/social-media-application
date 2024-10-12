package com.manager.socialmediaapplication.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("Generic Exception", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(SocialMediaApplicationException.class)
    public ResponseEntity<String> handleSocialMediaApplicationException(SocialMediaApplicationException ex) {
        log.error("Social Media Application Exception", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
