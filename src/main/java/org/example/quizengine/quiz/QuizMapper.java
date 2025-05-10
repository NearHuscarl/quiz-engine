package org.example.quizengine.quiz;

import org.example.quizengine.config.MapStructConfig;
import org.mapstruct.*;

@Mapper(config = MapStructConfig.class)
public interface QuizMapper {
    QuizEntity toEntity(QuizDto.Request request);

    QuizDto.Response toResponse(QuizEntity quiz);

    @Mapping(target = "id", source = "quiz.id")
    QuizDto.Completion toResponse(QuizCompletionEntity quizCompletion);
}
