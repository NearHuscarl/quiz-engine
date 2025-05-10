package org.example.quizengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.quizengine.auth.AuthDto;
import org.example.quizengine.quiz.QuizDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final AuthDto.Register[] users = new AuthDto.Register[]{
            new AuthDto.Register("test@mail.org", "password"),
    };
    private static final QuizDto.Request[] quizzes = new QuizDto.Request[]{
            new QuizDto.Request(
                    "The Java Logo",
                    "What is depicted on the Java logo?",
                    List.of("Robot", "Tea leaf", "Cup of coffee", "Bug"),
                    List.of(2)
            ),
            new QuizDto.Request(
                    "The Ultimate Question",
                    "What is the answer to the Ultimate Question of Life, the Universe and Everything?",
                    List.of("Everything goes right", "42", "2+2=4", "11011100"),
                    List.of(1)
            ),
            new QuizDto.Request(
                    "Math1",
                    "Which of the following is equal to 4?",
                    List.of("1+3", "2+2", "8-1", "1+5"),
                    List.of(0, 1)
            ),
    };

    @Test
    void quizLifecycleTest() throws Exception {
        register(users[0].email(), users[0].password());

        checkGetQuiz(0, 0);
        checkCreateQuiz(0, 0);
        checkGetQuiz(1, 0);
        checkDeleteQuiz(0, 0);
        checkGetQuiz(0, 0);
    }

    private void register(String email, String password) throws Exception {
        var user = new AuthDto.Register(email, password);
        var json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Registered successfully!"));
    }

    private void checkGetQuiz(int expectedQuizCount, int userId) throws Exception {
        var user = users[userId];

        mockMvc.perform(get("/api/quizzes?page=0").with(httpBasic(user.email(), user.password())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(expectedQuizCount));
    }

    private void checkCreateQuiz(int quizId, int userId) throws Exception {
        var user = users[userId];
        var quiz = quizzes[quizId];
        var json = objectMapper.writeValueAsString(quiz);

        mockMvc.perform(post("/api/quizzes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(httpBasic(user.email(), user.password()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value(quiz.title()))
                .andExpect(jsonPath("$.text").value(quiz.text()))
                .andExpect(jsonPath("$.options.length()").value(quiz.options().size()))
                .andExpect(jsonPath("$.answers").doesNotExist());
    }

    private void checkDeleteQuiz(int quizId, int userId) throws Exception {
        var user = users[userId];

        mockMvc.perform(delete("/api/quizzes/" + (quizId + 1)).with(httpBasic(user.email(), user.password())))
                .andExpect(status().isNoContent());
    }
}
