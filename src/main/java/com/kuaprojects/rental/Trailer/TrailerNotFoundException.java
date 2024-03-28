package com.kuaprojects.rental.Trailer;

public class TrailerNotFoundException extends RuntimeException{

    public TrailerNotFoundException(Long id) {
        super("Could not find a trailer: " + id);
    }
}
