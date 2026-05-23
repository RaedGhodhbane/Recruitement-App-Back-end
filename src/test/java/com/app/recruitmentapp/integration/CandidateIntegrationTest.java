package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Candidate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Candidate API - Integration Tests")
class CandidateIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("POST /candidate/registerCandidate should create a new candidate")
    void registerCandidate_shouldReturn201() {
        Map<String, String> request = Map.of(
                "email", "new@test.com",
                "password", "password123",
                "name", "John",
                "firstName", "Doe",
                "phone", "0612345678"
        );

        ResponseEntity<Candidate> response = restTemplate.postForEntity(
                "/candidate/registerCandidate", request, Candidate.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("new@test.com");
    }

    @Test
    @DisplayName("POST /candidate/registerCandidate should reject duplicate email")
    void registerCandidateWithDuplicateEmail_shouldReturn401() {
        createCandidate("duplicate@test.com");

        Map<String, String> request = Map.of(
                "email", "duplicate@test.com",
                "password", "password123",
                "name", "John",
                "firstName", "Doe",
                "phone", "0612345678"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/candidate/registerCandidate", request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("GET /candidate/candidates should return all candidates")
    void getAllCandidates_shouldReturnList() {
        createCandidate("alice@test.com");
        createCandidate("bob@test.com");

        ResponseEntity<List> response = restTemplate.getForEntity(
                "/candidate/candidates", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("GET /candidate/{id} should return candidate when found")
    void getCandidateById_whenFound_shouldReturn200() {
        Candidate saved = createCandidate("found@test.com");

        ResponseEntity<Candidate> response = restTemplate.getForEntity(
                "/candidate/" + saved.getId(), Candidate.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("found@test.com");
    }

    @Test
    @DisplayName("GET /candidate/{id} should return 404 when not found")
    void getCandidateById_whenNotFound_shouldReturn404() {
        ResponseEntity<Candidate> response = restTemplate.getForEntity(
                "/candidate/9999", Candidate.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /candidate/update/{id} should update candidate")
    void updateCandidate_shouldReturn200() {
        Candidate saved = createCandidate("update@test.com");
        Long candidateId = saved.getId();

        // Send full candidate JSON as body
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("name", "Updated");
        body.put("firstName", "Name");
        body.put("email", "update@test.com");

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<Map<String, Object>> entity = new org.springframework.http.HttpEntity<>(body, headers);

        ResponseEntity<Candidate> response = restTemplate.exchange(
                "/candidate/update/" + candidateId,
                org.springframework.http.HttpMethod.PUT,
                entity,
                Candidate.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Candidate updated = candidateRepository.findById(candidateId).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("DELETE /candidate/{id} should delete candidate")
    void deleteCandidate_shouldReturn200() {
        Candidate saved = createCandidate("delete@test.com");

        ResponseEntity<String> response = restTemplate.exchange(
                "/candidate/" + saved.getId(),
                org.springframework.http.HttpMethod.DELETE,
                null,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(candidateRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /candidate/{id} should return 404 when not found")
    void deleteCandidate_whenNotFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/candidate/9999",
                org.springframework.http.HttpMethod.DELETE,
                null,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
