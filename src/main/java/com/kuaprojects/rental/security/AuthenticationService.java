package com.kuaprojects.rental.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

@AllArgsConstructor
public class AuthenticationService {

    public static Authentication getAuthentication(String apiKey, ApiKeyService service) {
        if (!service.validateApiKey(apiKey)) throw new BadCredentialsException("Invalid API Key");

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
