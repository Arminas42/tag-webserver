package com.kuaprojects.rental.trailer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class TrailerNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(TrailerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String trailerNotFoundHandler(TrailerNotFoundException ex) {
        return ex.getMessage();
    }
}
