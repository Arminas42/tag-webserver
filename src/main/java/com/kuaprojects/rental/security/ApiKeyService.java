package com.kuaprojects.rental.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class ApiKeyService {
    public static final String API_KEY_CODE = "code";
    public static final String SCOPE = "scope";
    private final PasswordEncoder encoder;
    private final ApiKeyRepository repository;

    public boolean validateApiKey(String inputKey) {
        var mapOfInput = parseInput(inputKey);
        var returnedKeys = repository.findByScope(mapOfInput.get(SCOPE)).stream().findFirst();
        if (returnedKeys.isEmpty()) throw new BadCredentialsException("No such key for the scope");
        var keyToMatch = returnedKeys.get().getEncryptedKey();
        return encoder.matches(mapOfInput.get(API_KEY_CODE), keyToMatch);
    }

    private Map<String, String> parseInput(String inputKey) {
        String[] parts = inputKey.split("_");
        if (parts.length != 2) throw new BadCredentialsException("Key did not pass validation");
        return Map.of(API_KEY_CODE, parts[0], SCOPE, parts[1]);
    }
}
