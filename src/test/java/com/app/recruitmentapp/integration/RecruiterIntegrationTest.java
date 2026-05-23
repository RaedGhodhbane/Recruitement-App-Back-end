package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Recruiter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Recruiter API - Integration Tests")
class RecruiterIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("POST /recruiter/registerRecruiter should create a new recruiter")
    void registerRecruiter_shouldReturn200() {
        Map<String, String> request = Map.of(
                "email", "recruiter_new@test.com",
                "password", "password123",
                "name", "Jane",
                "firstName", "Smith",
                "phone", "0612345678"
        );

        ResponseEntity<Recruiter> response = restTemplate.postForEntity(
                "/recruiter/registerRecruiter", request, Recruiter.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("recruiter_new@test.com");
    }

    @Test
    @DisplayName("POST /recruiter/registerRecruiter should reject duplicate email")
    void registerRecruiterWithDuplicateEmail_shouldReturn401() {
        createRecruiter("dup_recruiter@test.com");

        Map<String, String> request = Map.of(
                "email", "dup_recruiter@test.com",
                "password", "password123",
                "name", "Jane",
                "firstName", "Smith",
                "phone", "0612345678"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/recruiter/registerRecruiter", request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("GET /recruiter/recruiters should return all recruiters")
    void getAllRecruiters_shouldReturnList() {
        createRecruiter("r1@test.com");
        createRecruiter("r2@test.com");

        ResponseEntity<List> response = restTemplate.getForEntity(
                "/recruiter/recruiters", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("GET /recruiter/{id} should return recruiter when found")
    void getRecruiterById_whenFound_shouldReturn200() {
        Recruiter saved = createRecruiter("found_recruiter@test.com");

        ResponseEntity<Recruiter> response = restTemplate.getForEntity(
                "/recruiter/" + saved.getId(), Recruiter.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("found_recruiter@test.com");
    }

    @Test
    @DisplayName("GET /recruiter/{id} should return 404 when not found")
    void getRecruiterById_whenNotFound_shouldReturn404() {
        ResponseEntity<Recruiter> response = restTemplate.getForEntity(
                "/recruiter/9999", Recruiter.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /recruiter/{id} should update recruiter")
    void updateRecruiter_shouldReturn200() {
        Recruiter saved = createRecruiter("update_recruiter@test.com");
        Recruiter updates = new Recruiter();
        updates.setName("Updated");
        updates.setCompanyName("New Corp");

        restTemplate.put("/recruiter/" + saved.getId(), updates);

        Recruiter updated = recruiterRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated");
        assertThat(updated.getCompanyName()).isEqualTo("New Corp");
    }

    @Test
    @DisplayName("DELETE /recruiter/{id} should delete recruiter")
    void deleteRecruiter_shouldReturn200() {
        Recruiter saved = createRecruiter("delete_recruiter@test.com");

        ResponseEntity<String> response = restTemplate.exchange(
                "/recruiter/" + saved.getId(),
                org.springframework.http.HttpMethod.DELETE,
                null,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recruiterRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /recruiter/{id} should return 404 when not found")
    void deleteRecruiter_whenNotFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/recruiter/9999",
                org.springframework.http.HttpMethod.DELETE,
                null,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
