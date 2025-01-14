package com.kuaprojects.rental.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class UserServiceImpl implements UserService {
//TODO: change return types, decide if this service is needed or just use UserDetailsService
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserDetailsService userDetailsService;
    @Override
    public boolean createUser(AppUserDTO dto) throws UserNotCreatedException {
        try {
            var user = userRepository.save(
                    AppUser.builder()
                            .username(dto.getUsername())
                            .password(encoder.encode(dto.getPassword()))
                            .role("ADMIN")
                            .build()
            );
        } catch (Exception e) {
            throw new UserNotCreatedException(dto.getUsername());
        }
        return true;
    }

    @Override
    public boolean isUserCredentialsCorrect(AppUserDTO dto) {
        var user = userDetailsService.loadUserByUsername(dto.getUsername());
        return encoder.matches(dto.getPassword(),user.getPassword());
    }

    @Override
    public boolean deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found" + id));
        userRepository.delete(user);
        return true;
    }
}
