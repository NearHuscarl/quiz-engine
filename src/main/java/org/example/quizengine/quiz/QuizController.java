package org.example.quizengine.quiz;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.quizengine.config.GlobalConfig;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(GlobalConfig.BASE_PATH)
public class QuizController {
    private final QuizService service;

    public QuizController(QuizService service) {
        this.service = service;
    }

    @GetMapping("quizzes")
    public ResponseEntity<Page<QuizDto.Response>> getQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        log.info("Get quizzes at page: {}", page);
        var body = service.getQuizzes(page);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("quizzes/completed")
    public ResponseEntity<Page<QuizDto.Completion>> getQuizCompletions(@RequestParam(defaultValue = "0") Integer page) {
        log.info("Get quiz completion at page: {}", page);
        var body = service.getQuizCompletions(page);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("quizzes/{id}")
    public ResponseEntity<QuizDto.Response> getQuiz(@PathVariable int id) {
        var quiz = service.findQuiz(id);

        log.info("Find quiz with id {}: {}", id, quiz);

        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping("quizzes")
    public ResponseEntity<QuizDto.Response> createQuiz(@RequestBody @Valid QuizDto.Request quiz) {
        log.debug("POST /api/quizzes: {}", quiz);
        var response = service.addQuiz(quiz);

        log.info("Created quiz: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("quizzes/{id}/solve")
    public ResponseEntity<QuizDto.Result> answerQuiz(@PathVariable int id, @RequestBody QuizDto.Answer body) {
        log.info("Quiz to answers: {}, The answers: {}", id, body.answers());

        if (!service.answerQuiz(id, body.answers())) {
            return new ResponseEntity<>(new QuizDto.Result(false, "Wrong answers! Please, try again."), HttpStatus.OK);
        }
        log.info("Answer correct");
        return new ResponseEntity<>(new QuizDto.Result(true, "Congratulations, you're right!"), HttpStatus.OK);
    }

    @DeleteMapping("quizzes/{id}")
    public ResponseEntity<QuizDto.Response> deleteQuiz(@PathVariable int id) throws JsonProcessingException {
        log.info("Deleting quiz with id {}", id);
        service.deleteQuiz(id);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}