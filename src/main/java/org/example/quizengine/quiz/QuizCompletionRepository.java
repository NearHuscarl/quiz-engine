package org.example.quizengine.quiz;

import org.example.quizengine.auth.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizCompletionRepository extends JpaRepository<QuizCompletionEntity, Long> {
    Page<QuizCompletionEntity> getAllByUser(UserEntity user, Pageable pageable);
}
