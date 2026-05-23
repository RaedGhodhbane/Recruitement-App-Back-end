package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Education;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Education API - Integration Tests")
class EducationIntegrationTest extends AbstractIntegrationTest {

    private String token;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = createCandidate("edu_candidate@test.com");
        token = generateToken("edu_candidate@test.com", "CANDIDATE");
    }

    @Test
    @DisplayName("GET /education/educations should return empty list initially")
    void getAllEducations_whenEmpty_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/education/educations", HttpMethod.GET, authHeader(token), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("POST /education/{idCandidate} should create education")
    void addEducation_shouldReturn201() {
        Education education = new Education();
        education.setDiploma("Master");
        education.setUniversity("University Paris");
        education.setDescription("Computer Science");

        ResponseEntity<Education> response = restTemplate.exchange(
                "/education/" + candidate.getId(),
                HttpMethod.POST,
                authEntity(education, token),
                Education.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDiploma()).isEqualTo("Master");
    }

    @Test
    @DisplayName("GET /education/{id} should return education when found")
    void getEducationById_whenFound_shouldReturn200() {
        Education saved = educationRepository.save(Education.builder()
                .diploma("Bachelor")
                .university("Test Univ")
                .description("Science")
                .candidate(candidate)
                .build());

        ResponseEntity<Education> response = restTemplate.exchange(
                "/education/" + saved.getId(),
                HttpMethod.GET,
                authHeader(token),
                Education.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDiploma()).isEqualTo("Bachelor");
    }

    @Test
    @DisplayName("GET /education/{id} should return 404 when not found")
    void getEducationById_whenNotFound_shouldReturn404() {
        ResponseEntity<Education> response = restTemplate.exchange(
                "/education/9999", HttpMethod.GET, authHeader(token), Education.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /education/{id} should update education")
    void updateEducation_shouldReturn200() {
        Education saved = educationRepository.save(Education.builder()
                .diploma("Bachelor")
                .university("Old Univ")
                .candidate(candidate)
                .build());

        Education updates = new Education();
        updates.setDiploma("Master");
        updates.setUniversity("New Univ");

        ResponseEntity<Education> response = restTemplate.exchange(
                "/education/" + saved.getId(),
                HttpMethod.PUT,
                authEntity(updates, token),
                Education.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDiploma()).isEqualTo("Master");
    }

    @Test
    @DisplayName("PUT /education/{id} should return 404 when not found")
    void updateEducation_whenNotFound_shouldReturn404() {
        Education updates = new Education();
        updates.setDiploma("Master");

        ResponseEntity<Education> response = restTemplate.exchange(
                "/education/9999", HttpMethod.PUT, authEntity(updates, token), Education.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE /education/{id} should delete education")
    void deleteEducation_shouldReturn200() {
        Education saved = educationRepository.save(Education.builder()
                .diploma("PhD")
                .university("Test Univ")
                .candidate(candidate)
                .build());

        ResponseEntity<Map> response = restTemplate.exchange(
                "/education/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(token),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(educationRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /education/{id} should return 404 when not found")
    void deleteEducation_whenNotFound_shouldReturn404() {
        ResponseEntity<Map> response = restTemplate.exchange(
                "/education/9999", HttpMethod.DELETE, authHeader(token), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
