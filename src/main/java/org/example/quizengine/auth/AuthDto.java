package org.example.quizengine.auth;

import jakarta.validation.constraints.*;

public class AuthDto {
    public record Register(
            // Note: for the exercise, @Email is not good enough - doesn't check for the dot delimiter.
            @Pattern(regexp = "\\w+@\\w+\\.\\w+", message = "Email format must be: <username>@<domain>.<extension>")
            String email,
            @Size(min = 5, message = "Password must have at least 5 characters")
            String password
    ) {}
    public record Response(long id, String email) {}
}
