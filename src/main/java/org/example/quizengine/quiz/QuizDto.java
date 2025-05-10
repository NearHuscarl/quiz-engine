package org.example.quizengine.quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

public class QuizDto {
    public record Request(
            @NotBlank(message = "title of Quiz is required")
            String title,
            @NotBlank(message = "text of Quiz is required")
            String text,

            @NotNull(message = "quiz must defined a list of options")
            @Size(min = 2, message = "quiz must have at least 2 options")
            List<String> options,

            List<Integer> answers
    ) {}

    public record Response(int id, String title, String text, List<String> options) {}

    public record Answer(List<Integer> answers) {}

    public record Result(boolean success, String feedback) {}

    public record Completion(long id, Date completedAt) {}
}