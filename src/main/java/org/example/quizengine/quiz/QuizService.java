package org.example.quizengine.quiz;

import lombok.extern.slf4j.Slf4j;
import org.example.quizengine.auth.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QuizService {
    private final QuizMapper mapper;
    private final QuizRepository repository;
    private final QuizCompletionRepository completionRepository;
    private final AuthService authService;

    public QuizService(QuizMapper mapper, QuizRepository quizRepository, QuizCompletionRepository completionRepository, AuthService authService) {
        this.mapper = mapper;
        this.repository = quizRepository;
        this.completionRepository = completionRepository;
        this.authService = authService;
    }

    public Page<QuizDto.Response> getQuizzes(int page) {
        var quizPage = repository.findAll(PageRequest.of(page, 10));
        return quizPage.map(mapper::toResponse);
    }

    public QuizDto.Response addQuiz(QuizDto.Request quiz) {
        var quizEntity = mapper.toEntity(quiz);
        var userEntity = authService.getCurrentUser();

        quizEntity.setUser(userEntity);

        var newQuiz = repository.save(quizEntity);
        return mapper.toResponse(newQuiz);
    }

    public QuizDto.Response findQuiz(long id) {
        log.debug("Finding Quiz with id {}", id);
        return mapper.toResponse(this.findQuizEntity(id));
    }

    public boolean answerQuiz(long quizId, List<Integer> answers) {
        var quizEntity = this.findQuizEntity(quizId);
        var quizAnswer = new ArrayList<>(quizEntity.getAnswers());

        log.debug("Check answer: Expected {}, got: {}", quizEntity.getAnswers(), answers);

        var isAnswerCorrect = quizAnswer.equals(answers);

        if (isAnswerCorrect) {
            var completionEntity = new QuizCompletionEntity();
            var userEntity = authService.getCurrentUser();

            completionEntity.setQuiz(quizEntity);
            completionEntity.setUser(userEntity);
            completionRepository.save(completionEntity);
        }

        return isAnswerCorrect;
    }

    public Page<QuizDto.Completion> getQuizCompletions(int page) {
        var pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "completedAt"));
        var userEntity = authService.getCurrentUser();
        var completionPage = completionRepository.getAllByUser(userEntity, pageRequest);

        return completionPage.map(mapper::toResponse);
    }

    private QuizEntity findQuizEntity(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    public void deleteQuiz(long id) {
        var quiz = this.findQuizEntity(id);
        var currentUser = authService.getCurrentUser();

        if (!quiz.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to delete this quiz");
        }

        repository.deleteById(id);
    }
}
