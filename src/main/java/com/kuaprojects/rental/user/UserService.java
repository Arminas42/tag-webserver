package com.kuaprojects.rental.user;

public interface UserService {
    boolean createUser (AppUserDTO dto);
    boolean isUserCredentialsCorrect (AppUserDTO dto);
    boolean deleteUser (Long id);
}
