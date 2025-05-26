package com.kuaprojects.rental.user;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String username) {
        super("User already exists: " + username);
    }

}
