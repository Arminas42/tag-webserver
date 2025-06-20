package com.kuaprojects.rental.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppUserDTO {
    private String username;
    private String password;
}
