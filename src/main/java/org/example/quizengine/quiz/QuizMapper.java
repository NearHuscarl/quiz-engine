package org.example.quizengine.quiz;

import org.example.quizengine.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.Mappings;

@Mapper(config = MapStructConfig.class)
public interface QuizMapper {
    QuizEntity toEntity(QuizDto.Request request);
    QuizDto.Response toResponse(QuizEntity quiz);

    @Mapping(target = "id", expression = "java(quizCompletion.getQuiz().getId())")
    QuizDto.Completion toResponse(QuizCompletionEntity quizCompletion);
}
