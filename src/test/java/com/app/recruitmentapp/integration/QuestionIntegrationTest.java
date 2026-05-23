package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Question;
import com.app.recruitmentapp.entities.Recruiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Question API - Integration Tests")
class QuestionIntegrationTest extends AbstractIntegrationTest {

    private Recruiter recruiter;
    private Offer offer;
    private String recruiterToken;

    @BeforeEach
    void setUp() {
        recruiter = createRecruiter("q_recruiter@test.com");
        recruiterToken = generateToken("q_recruiter@test.com", "RECRUITER");
        offer = offerRepository.save(Offer.builder()
                .title("Question Offer")
                .description("Offer with questions")
                .recruiter(recruiter)
                .build());
    }

    @Test
    @DisplayName("GET /question/questions should return empty list initially")
    void getAllQuestions_whenEmpty_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/question/questions", HttpMethod.GET, authHeader(recruiterToken), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("POST /question/{idOffer} should create a question")
    void addQuestion_shouldReturn201() {
        Question question = new Question();
        question.setTitle("What is Java?");
        question.setChoice1("A language");
        question.setChoice2("A coffee");
        question.setChoice3("An island");
        question.setResponse("A language");

        ResponseEntity<String> response = restTemplate.exchange(
                "/question/" + offer.getId(),
                HttpMethod.POST,
                authEntity(question, recruiterToken),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /question/{id} should return question when found")
    void getQuestionById_whenFound_shouldReturn200() {
        Question saved = questionRepository.save(Question.builder()
                .title("Question?")
                .choice1("A")
                .choice2("B")
                .choice3("C")
                .response("A")
                .offer(offer)
                .build());

        ResponseEntity<String> response = restTemplate.exchange(
                "/question/" + saved.getId(),
                HttpMethod.GET,
                authHeader(recruiterToken),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Question?");
    }

    @Test
    @DisplayName("GET /question/{id} should return 404 when not found")
    void getQuestionById_whenNotFound_shouldReturn404() {
        ResponseEntity<Question> response = restTemplate.exchange(
                "/question/9999", HttpMethod.GET, authHeader(recruiterToken), Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /question/{id} should update question")
    void updateQuestion_shouldReturn200() {
        Question saved = questionRepository.save(Question.builder()
                .title("Old question?")
                .choice1("A")
                .choice2("B")
                .choice3("C")
                .response("A")
                .offer(offer)
                .build());

        Question updates = new Question();
        updates.setTitle("New question?");
        updates.setResponse("B");

        ResponseEntity<String> response = restTemplate.exchange(
                "/question/" + saved.getId(),
                HttpMethod.PUT,
                authEntity(updates, recruiterToken),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("New question?");
    }

    @Test
    @DisplayName("PUT /question/{id} should return 404 when not found")
    void updateQuestion_whenNotFound_shouldReturn404() {
        Question updates = new Question();
        updates.setTitle("No matter");

        ResponseEntity<String> response = restTemplate.exchange(
                "/question/9999", HttpMethod.PUT, authEntity(updates, recruiterToken), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE /question/{id} should delete question")
    void deleteQuestion_shouldReturn200() {
        Question saved = questionRepository.save(Question.builder()
                .title("To delete?")
                .choice1("A")
                .choice2("B")
                .choice3("C")
                .response("A")
                .offer(offer)
                .build());

        ResponseEntity<String> response = restTemplate.exchange(
                "/question/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(recruiterToken),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
        assertThat(questionRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /question/{id} should return 404 when not found")
    void deleteQuestion_whenNotFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/question/9999", HttpMethod.DELETE, authHeader(recruiterToken), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
