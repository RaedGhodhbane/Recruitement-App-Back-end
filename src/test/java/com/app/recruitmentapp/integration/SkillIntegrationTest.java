package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Skill API - Integration Tests")
class SkillIntegrationTest extends AbstractIntegrationTest {

    private String token;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = createCandidate("skill_auth@test.com");
        token = generateToken("skill_auth@test.com", "CANDIDATE");
    }

    @Test
    @DisplayName("GET /skill/skills should return empty list initially")
    void getAllSkills_whenEmpty_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/skill/skills", HttpMethod.GET, authHeader(token), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("POST /skill/{idCandidate} should create a skill for candidate")
    void addSkill_shouldReturn201() {
        Skill skill = new Skill();
        skill.setTitle("Java");
        skill.setPercentage("90%");

        ResponseEntity<Skill> response = restTemplate.exchange(
                "/skill/" + candidate.getId(),
                HttpMethod.POST,
                authEntity(skill, token),
                Skill.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Java");
    }

    @Test
    @DisplayName("GET /skill/{id} should return skill when found")
    void getSkillById_whenFound_shouldReturn200() {
        Skill saved = skillRepository.save(Skill.builder()
                .title("Spring Boot")
                .percentage("85%")
                .candidate(candidate)
                .build());

        ResponseEntity<Skill> response = restTemplate.exchange(
                "/skill/" + saved.getId(),
                HttpMethod.GET,
                authHeader(token),
                Skill.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Spring Boot");
    }

    @Test
    @DisplayName("GET /skill/{id} should return 404 when not found")
    void getSkillById_whenNotFound_shouldReturn404() {
        ResponseEntity<Skill> response = restTemplate.exchange(
                "/skill/9999",
                HttpMethod.GET,
                authHeader(token),
                Skill.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /skill/{id} should update skill")
    void updateSkill_shouldReturn200() {
        Skill saved = skillRepository.save(Skill.builder()
                .title("Java")
                .percentage("50%")
                .candidate(candidate)
                .build());

        Skill updates = new Skill();
        updates.setTitle("Java Avancé");
        updates.setPercentage("90%");

        ResponseEntity<Skill> response = restTemplate.exchange(
                "/skill/" + saved.getId(),
                HttpMethod.PUT,
                authEntity(updates, token),
                Skill.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Java Avancé");
    }

    @Test
    @DisplayName("DELETE /skill/{id} should delete skill")
    void deleteSkill_shouldReturn200() {
        Skill saved = skillRepository.save(Skill.builder()
                .title("Python")
                .percentage("80%")
                .candidate(candidate)
                .build());

        ResponseEntity<Map> response = restTemplate.exchange(
                "/skill/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(token),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(skillRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /skill/{id} should return 404 when not found")
    void deleteSkill_whenNotFound_shouldReturn404() {
        ResponseEntity<Map> response = restTemplate.exchange(
                "/skill/9999",
                HttpMethod.DELETE,
                authHeader(token),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
