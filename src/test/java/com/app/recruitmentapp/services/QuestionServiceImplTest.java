package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.QuestionDTO;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Question;
import com.app.recruitmentapp.mapper.EntityMapper;
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
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private QuestionServiceImpl questionService;

    private Offer offer;
    private Question question1;
    private Question question2;
    private QuestionDTO question1DTO;
    private QuestionDTO question2DTO;

    @BeforeEach
    void setUp() {
        offer = new Offer();
        offer.setId(1L);
        offer.setTitle("Software Engineer");

        question1 = new Question();
        question1.setId(1L);
        question1.setTitle("Quel est votre langage prefere ?");
        question1.setChoice1("Java");
        question1.setChoice2("Python");
        question1.setChoice3("JavaScript");
        question1.setResponse("Java");
        question1.setOffer(offer);

        question2 = new Question();
        question2.setId(2L);
        question2.setTitle("Annees d'experience ?");
        question2.setChoice1("1-2");
        question2.setChoice2("3-5");
        question2.setChoice3("5+");
        question2.setResponse("3-5");
        question2.setOffer(offer);

        question1DTO = new QuestionDTO();
        question1DTO.setId(1L);
        question1DTO.setTitle("Quel est votre langage prefere ?");
        question1DTO.setChoice1("Java");
        question1DTO.setChoice2("Python");
        question1DTO.setChoice3("JavaScript");
        question1DTO.setResponse("Java");
        question1DTO.setOfferId(1L);

        question2DTO = new QuestionDTO();
        question2DTO.setId(2L);
        question2DTO.setTitle("Annees d'experience ?");
        question2DTO.setChoice1("1-2");
        question2DTO.setChoice2("3-5");
        question2DTO.setChoice3("5+");
        question2DTO.setResponse("3-5");
        question2DTO.setOfferId(1L);
    }

    @Nested
    @DisplayName("getAllQuestions Tests")
    class GetAllQuestionsTests {

        @Test
        @DisplayName("Should return all questions")
        void shouldGetAllQuestionsSuccessfully() {
            List<Question> mockQuestions = List.of(question1, question2);
            when(questionRepository.findAll()).thenReturn(mockQuestions);
            when(entityMapper.toQuestionDTOList(mockQuestions)).thenReturn(List.of(question1DTO, question2DTO));

            List<QuestionDTO> result = questionService.getAllQuestions();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Quel est votre langage prefere ?", result.get(0).getTitle());
            assertEquals("Annees d'experience ?", result.get(1).getTitle());
            verify(questionRepository).findAll();
            verify(entityMapper).toQuestionDTOList(mockQuestions);
        }
    }

    @Nested
    @DisplayName("getQuestionById Tests")
    class GetQuestionByIdTests {

        @Test
        @DisplayName("Should return question when found")
        void shouldReturnQuestionWhenFound() {
            when(questionRepository.findById(1L)).thenReturn(Optional.of(question1));
            when(entityMapper.toQuestionDTO(question1)).thenReturn(question1DTO);

            Optional<QuestionDTO> result = questionService.getQuestionById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("Java", result.get().getResponse());
            verify(questionRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(questionRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<QuestionDTO> result = questionService.getQuestionById(99L);

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
            QuestionDTO newQuestionDTO = new QuestionDTO();
            newQuestionDTO.setTitle("Base de donnees preferee ?");
            newQuestionDTO.setChoice1("MySQL");
            newQuestionDTO.setChoice2("PostgreSQL");
            newQuestionDTO.setChoice3("MongoDB");
            newQuestionDTO.setResponse("PostgreSQL");

            Question newQuestionEntity = new Question();
            newQuestionEntity.setTitle("Base de donnees preferee ?");
            newQuestionEntity.setChoice1("MySQL");
            newQuestionEntity.setChoice2("PostgreSQL");
            newQuestionEntity.setChoice3("MongoDB");
            newQuestionEntity.setResponse("PostgreSQL");

            QuestionDTO resultDTO = new QuestionDTO();
            resultDTO.setId(3L);
            resultDTO.setTitle("Base de donnees preferee ?");
            resultDTO.setChoice1("MySQL");
            resultDTO.setChoice2("PostgreSQL");
            resultDTO.setChoice3("MongoDB");
            resultDTO.setResponse("PostgreSQL");
            resultDTO.setOfferId(1L);

            when(entityMapper.toQuestionEntity(newQuestionDTO)).thenReturn(newQuestionEntity);
            when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
            when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> {
                Question saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toQuestionDTO(any(Question.class))).thenReturn(resultDTO);

            QuestionDTO result = questionService.saveQuestion(newQuestionDTO, 1L);

            assertNotNull(result);
            assertEquals("Base de donnees preferee ?", result.getTitle());
            assertEquals("PostgreSQL", result.getResponse());
            assertEquals(1L, result.getOfferId());
            verify(entityMapper).toQuestionEntity(newQuestionDTO);
            verify(offerRepository).findById(1L);
            verify(questionRepository).save(any(Question.class));
            verify(entityMapper).toQuestionDTO(any(Question.class));
        }

        @Test
        @DisplayName("Should save question with null offer when offer not found")
        void shouldSaveQuestionWithNullOfferWhenNotFound() {
            QuestionDTO newQuestionDTO = new QuestionDTO();
            newQuestionDTO.setTitle("Base de donnees preferee ?");
            newQuestionDTO.setResponse("PostgreSQL");

            Question newQuestionEntity = new Question();
            newQuestionEntity.setTitle("Base de donnees preferee ?");
            newQuestionEntity.setResponse("PostgreSQL");

            QuestionDTO resultDTO = new QuestionDTO();
            resultDTO.setId(3L);
            resultDTO.setTitle("Base de donnees preferee ?");
            resultDTO.setResponse("PostgreSQL");

            when(entityMapper.toQuestionEntity(newQuestionDTO)).thenReturn(newQuestionEntity);
            when(offerRepository.findById(99L)).thenReturn(Optional.empty());
            when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> {
                Question saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toQuestionDTO(any(Question.class))).thenReturn(resultDTO);

            QuestionDTO result = questionService.saveQuestion(newQuestionDTO, 99L);

            assertNotNull(result);
            assertNull(result.getOfferId());
            verify(entityMapper).toQuestionEntity(newQuestionDTO);
            verify(offerRepository).findById(99L);
            verify(questionRepository).save(any(Question.class));
            verify(entityMapper).toQuestionDTO(any(Question.class));
        }
    }

    @Nested
    @DisplayName("updateQuestion Tests")
    class UpdateQuestionTests {

        @Test
        @DisplayName("Should update question when found")
        void shouldUpdateQuestionWhenFound() {
            QuestionDTO updatedData = new QuestionDTO();
            updatedData.setTitle("Framework prefere ?");
            updatedData.setChoice1("Spring");
            updatedData.setChoice2("Django");
            updatedData.setChoice3("Express");
            updatedData.setResponse("Spring");

            QuestionDTO resultDTO = new QuestionDTO();
            resultDTO.setId(1L);
            resultDTO.setTitle("Framework prefere ?");
            resultDTO.setChoice1("Spring");
            resultDTO.setChoice2("Django");
            resultDTO.setChoice3("Express");
            resultDTO.setResponse("Spring");
            resultDTO.setOfferId(1L);

            when(questionRepository.findById(1L)).thenReturn(Optional.of(question1));
            when(questionRepository.saveAndFlush(question1)).thenReturn(question1);
            when(entityMapper.toQuestionDTO(question1)).thenReturn(resultDTO);

            QuestionDTO result = questionService.updateQuestion(1L, updatedData);

            assertNotNull(result);
            assertEquals("Framework prefere ?", result.getTitle());
            assertEquals("Spring", result.getChoice1());
            assertEquals("Django", result.getChoice2());
            assertEquals("Express", result.getChoice3());
            assertEquals("Spring", result.getResponse());
            assertEquals(1L, result.getOfferId());
            verify(questionRepository).findById(1L);
            verify(questionRepository).saveAndFlush(question1);
            verify(entityMapper).toQuestionDTO(question1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when question not found")
        void shouldThrowWhenQuestionNotFound() {
            QuestionDTO updatedData = new QuestionDTO();
            updatedData.setTitle("Framework prefere ?");

            when(questionRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
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
