package com.kuaprojects.rental.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppUserControllerAdvice {

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<String> userAlreadyExistsHandler(UserAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
