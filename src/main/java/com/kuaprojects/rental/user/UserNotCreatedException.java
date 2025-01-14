package com.kuaprojects.rental.user;

public class UserNotCreatedException extends RuntimeException{
    public UserNotCreatedException(String username) {
        super("Could not create user: " + username);
    }

}
