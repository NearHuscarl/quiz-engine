package org.example.quizengine.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final AuthRepository repository;
    private final AuthMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository repository, AuthMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthDto.Response addUser(AuthDto.Register registerDto) {
        if (repository.existsByEmail(registerDto.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        var userEntity = this.mapper.toEntity(registerDto, this.passwordEncoder);
        var newUserEntity = repository.save(userEntity);

        return this.mapper.toResponse(newUserEntity);
    }

    public UserEntity getCurrentUser() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
