package com.kuaprojects.rental.trailer;

public class TrailerNotFoundException extends RuntimeException{

    public TrailerNotFoundException(Long id) {
        super("Could not find a trailer: " + id);
    }
}
