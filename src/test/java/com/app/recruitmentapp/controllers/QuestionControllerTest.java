package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Question;
import com.app.recruitmentapp.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionController Unit Tests")
class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    private Question testQuestion;

    @BeforeEach
    void setUp() {
        testQuestion = new Question();
        testQuestion.setId(1L);
        testQuestion.setTitle("What is the work schedule?");
    }

    @Nested
    @DisplayName("GET /question/questions")
    class GetAllQuestionsTests {

        @Test
        @DisplayName("Should return all questions")
        void shouldReturnAllQuestions() {
            when(questionService.getAllQuestions()).thenReturn(List.of(testQuestion));

            List<Question> result = questionController.getAllQuestions();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /question/{id}")
    class GetQuestionByIdTests {

        @Test
        @DisplayName("Should return question when found")
        void shouldReturnQuestionWhenFound() {
            when(questionService.getQuestionById(1L)).thenReturn(Optional.of(testQuestion));

            ResponseEntity<Question> response = questionController.getQuestionById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(questionService.getQuestionById(99L)).thenReturn(Optional.empty());

            ResponseEntity<Question> response = questionController.getQuestionById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /question/{idOffer}")
    class AddQuestionTests {

        @Test
        @DisplayName("Should add question successfully")
        void shouldAddQuestionSuccessfully() {
            when(questionService.saveQuestion(testQuestion, 1L)).thenReturn(testQuestion);

            ResponseEntity<Question> response = questionController.addQuestion(testQuestion, 1L);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /question/{id}")
    class UpdateQuestionTests {

        @Test
        @DisplayName("Should update question successfully")
        void shouldUpdateQuestionSuccessfully() {
            when(questionService.updateQuestion(1L, testQuestion)).thenReturn(testQuestion);

            ResponseEntity<Question> response = questionController.updateQuestion(1L, testQuestion);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(questionService.updateQuestion(99L, testQuestion)).thenThrow(new RuntimeException("Question not found"));

            ResponseEntity<Question> response = questionController.updateQuestion(99L, testQuestion);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /question/{id}")
    class DeleteQuestionTests {

        @Test
        @DisplayName("Should delete question successfully")
        void shouldDeleteQuestionSuccessfully() {
            doNothing().when(questionService).deleteQuestion(1L);

            ResponseEntity<String> response = questionController.deleteQuestion(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Question deleted successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Question not found")).when(questionService).deleteQuestion(99L);

            ResponseEntity<String> response = questionController.deleteQuestion(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
