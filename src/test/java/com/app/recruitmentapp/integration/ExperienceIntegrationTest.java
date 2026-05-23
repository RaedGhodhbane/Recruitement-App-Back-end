package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Experience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Experience API - Integration Tests")
class ExperienceIntegrationTest extends AbstractIntegrationTest {

    private String token;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = createCandidate("exp_candidate@test.com");
        token = generateToken("exp_candidate@test.com", "CANDIDATE");
    }

    @Test
    @DisplayName("GET /experience/experiences should return empty list initially")
    void getAllExperiences_whenEmpty_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/experience/experiences", HttpMethod.GET, authHeader(token), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("POST /experience/{idCandidate} should create experience")
    void addExperience_shouldReturn201() {
        Experience experience = new Experience();
        experience.setCompanyName("Google");
        experience.setJobTitle("Engineer");
        experience.setDescription("Full stack dev");

        ResponseEntity<Experience> response = restTemplate.exchange(
                "/experience/" + candidate.getId(),
                HttpMethod.POST,
                authEntity(experience, token),
                Experience.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCompanyName()).isEqualTo("Google");
    }

    @Test
    @DisplayName("GET /experience/{id} should return experience when found")
    void getExperienceById_whenFound_shouldReturn200() {
        Experience saved = experienceRepository.save(Experience.builder()
                .companyName("Microsoft")
                .jobTitle("Developer")
                .candidate(candidate)
                .build());

        ResponseEntity<Experience> response = restTemplate.exchange(
                "/experience/" + saved.getId(),
                HttpMethod.GET,
                authHeader(token),
                Experience.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCompanyName()).isEqualTo("Microsoft");
    }

    @Test
    @DisplayName("GET /experience/{id} should return 404 when not found")
    void getExperienceById_whenNotFound_shouldReturn404() {
        ResponseEntity<Experience> response = restTemplate.exchange(
                "/experience/9999", HttpMethod.GET, authHeader(token), Experience.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /experience/{id} should update experience")
    void updateExperience_shouldReturn200() {
        Experience saved = experienceRepository.save(Experience.builder()
                .companyName("Old Corp")
                .jobTitle("Junior")
                .candidate(candidate)
                .build());

        Experience updates = new Experience();
        updates.setCompanyName("New Corp");
        updates.setJobTitle("Senior");

        ResponseEntity<Experience> response = restTemplate.exchange(
                "/experience/" + saved.getId(),
                HttpMethod.PUT,
                authEntity(updates, token),
                Experience.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCompanyName()).isEqualTo("New Corp");
    }

    @Test
    @DisplayName("PUT /experience/{id} should return 404 when not found")
    void updateExperience_whenNotFound_shouldReturn404() {
        Experience updates = new Experience();
        updates.setCompanyName("New Corp");

        ResponseEntity<Experience> response = restTemplate.exchange(
                "/experience/9999", HttpMethod.PUT, authEntity(updates, token), Experience.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE /experience/{id} should delete experience")
    void deleteExperience_shouldReturn200() {
        Experience saved = experienceRepository.save(Experience.builder()
                .companyName("Startup")
                .jobTitle("Dev")
                .candidate(candidate)
                .build());

        ResponseEntity<Map> response = restTemplate.exchange(
                "/experience/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(token),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(experienceRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /experience/{id} should return 404 when not found")
    void deleteExperience_whenNotFound_shouldReturn404() {
        ResponseEntity<Map> response = restTemplate.exchange(
                "/experience/9999", HttpMethod.DELETE, authHeader(token), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
