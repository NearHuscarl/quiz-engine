package org.example.quizengine.auth;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.quizengine.config.GlobalConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(GlobalConfig.BASE_PATH)
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody @Valid AuthDto.Register request) {
        var user = authService.addUser(request);

        log.info("Registering user: {}", user);

        return new ResponseEntity<>("Registered successfully!", HttpStatus.OK);
    }
}