package org.example.quizengine.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
}
