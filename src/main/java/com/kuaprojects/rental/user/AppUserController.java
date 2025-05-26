package com.kuaprojects.rental.user;

import com.kuaprojects.rental.configuration.ApiPrefixController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@AllArgsConstructor
@ApiPrefixController
public class AppUserController {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    @PostMapping("/user")
    ResponseEntity<String> createUser(@RequestBody AppUserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) throw new UserAlreadyExistsException(dto.getUsername());
        var user = userRepository.save(
                AppUser.builder()
                        .username(dto.getUsername())
                        .password(encoder.encode(dto.getPassword()))
                        .role("ROLE_ADMIN")
                        .build()
        );
        return ResponseEntity.ok("User created!");
    }

    @GetMapping("/user")
    ResponseEntity<List<AppUser>> getUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }
}
