package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Question;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionServiceImpl Unit Tests")
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private OfferRepository offerRepository;
    @InjectMocks
    private QuestionServiceImpl questionService;

    private Offer offer;
    private Question question1;
    private Question question2;

    @BeforeEach
    void setUp() {
        offer = new Offer();
        offer.setId(1L);
        offer.setTitle("Software Engineer");

        question1 = new Question();
        question1.setId(1L);
        question1.setTitle("Quel est votre langage préféré ?");
        question1.setChoice1("Java");
        question1.setChoice2("Python");
        question1.setChoice3("JavaScript");
        question1.setResponse("Java");
        question1.setOffer(offer);

        question2 = new Question();
        question2.setId(2L);
        question2.setTitle("Années d'expérience ?");
        question2.setChoice1("1-2");
        question2.setChoice2("3-5");
        question2.setChoice3("5+");
        question2.setResponse("3-5");
        question2.setOffer(offer);
    }

    @Nested
    @DisplayName("getAllQuestions Tests")
    class GetAllQuestionsTests {

        @Test
        @DisplayName("Should return all questions")
        void shouldGetAllQuestionsSuccessfully() {
            List<Question> mockQuestions = List.of(question1, question2);
            when(questionRepository.findAll()).thenReturn(mockQuestions);

            List<Question> result = questionService.getAllQuestions();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Quel est votre langage préféré ?", result.get(0).getTitle());
            assertEquals("Années d'expérience ?", result.get(1).getTitle());
            verify(questionRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getQuestionById Tests")
    class GetQuestionByIdTests {

        @Test
        @DisplayName("Should return question when found")
        void shouldReturnQuestionWhenFound() {
            when(questionRepository.findById(1L)).thenReturn(Optional.of(question1));

            Optional<Question> result = questionService.getQuestionById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("Java", result.get().getResponse());
            verify(questionRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(questionRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Question> result = questionService.getQuestionById(99L);

            assertFalse(result.isPresent());
            verify(questionRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("saveQuestion Tests")
    class SaveQuestionTests {

        @Test
        @DisplayName("Should save question successfully")
        void shouldSaveQuestionSuccessfully() {
            Question newQuestion = new Question();
            newQuestion.setTitle("Base de données préférée ?");
            newQuestion.setChoice1("MySQL");
            newQuestion.setChoice2("PostgreSQL");
            newQuestion.setChoice3("MongoDB");
            newQuestion.setResponse("PostgreSQL");

            when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
            when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> {
                Question saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Question result = questionService.saveQuestion(newQuestion, 1L);

            assertNotNull(result);
            assertEquals("Base de données préférée ?", result.getTitle());
            assertEquals("PostgreSQL", result.getResponse());
            assertEquals(offer, result.getOffer());
            verify(offerRepository).findById(1L);
            verify(questionRepository).save(any(Question.class));
        }

        @Test
        @DisplayName("Should save question with null offer when offer not found")
        void shouldSaveQuestionWithNullOfferWhenNotFound() {
            Question newQuestion = new Question();
            newQuestion.setTitle("Base de données préférée ?");
            newQuestion.setResponse("PostgreSQL");

            when(offerRepository.findById(99L)).thenReturn(Optional.empty());
            when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> {
                Question saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Question result = questionService.saveQuestion(newQuestion, 99L);

            assertNotNull(result);
            assertNull(result.getOffer());
            verify(offerRepository).findById(99L);
            verify(questionRepository).save(any(Question.class));
        }
    }

    @Nested
    @DisplayName("updateQuestion Tests")
    class UpdateQuestionTests {

        @Test
        @DisplayName("Should update question when found")
        void shouldUpdateQuestionWhenFound() {
            Question updatedData = new Question();
            updatedData.setTitle("Framework préféré ?");
            updatedData.setChoice1("Spring");
            updatedData.setChoice2("Django");
            updatedData.setChoice3("Express");
            updatedData.setResponse("Spring");
            updatedData.setOffer(offer);

            when(questionRepository.findById(1L)).thenReturn(Optional.of(question1));
            when(questionRepository.saveAndFlush(question1)).thenReturn(question1);

            Question result = questionService.updateQuestion(1L, updatedData);

            assertNotNull(result);
            assertEquals("Framework préféré ?", result.getTitle());
            assertEquals("Spring", result.getChoice1());
            assertEquals("Django", result.getChoice2());
            assertEquals("Express", result.getChoice3());
            assertEquals("Spring", result.getResponse());
            assertEquals(offer, result.getOffer());
            verify(questionRepository).findById(1L);
            verify(questionRepository).saveAndFlush(question1);
        }

        @Test
        @DisplayName("Should throw NullPointerException when question not found")
        void shouldThrowWhenQuestionNotFound() {
            Question updatedData = new Question();
            updatedData.setTitle("Framework préféré ?");

            when(questionRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(NullPointerException.class,
                    () -> questionService.updateQuestion(99L, updatedData));
            verify(questionRepository).findById(99L);
            verify(questionRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("deleteQuestion Tests")
    class DeleteQuestionTests {

        @Test
        @DisplayName("Should delete question when found")
        void shouldDeleteQuestionWhenFound() {
            when(questionRepository.existsById(1L)).thenReturn(true);

            questionService.deleteQuestion(1L);

            verify(questionRepository).existsById(1L);
            verify(questionRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenQuestionNotFound() {
            when(questionRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> questionService.deleteQuestion(99L));
            verify(questionRepository).existsById(99L);
            verify(questionRepository, never()).deleteById(anyLong());
        }
    }
}
