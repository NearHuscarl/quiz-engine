package org.example.quizengine.quiz;

import jakarta.persistence.*;
import lombok.Data;
import org.example.quizengine.auth.UserEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
public class QuizCompletionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "quiz_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private QuizEntity quiz;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @CreationTimestamp
    private Date completedAt;
}

