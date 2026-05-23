package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Candidacy API - Integration Tests")
class CandidacyIntegrationTest extends AbstractIntegrationTest {

    private String candidateToken;
    private String recruiterToken;
    private Candidate candidate;
    private Recruiter recruiter;
    private Offer offer;

    @BeforeEach
    void setUp() {
        candidate = createCandidate("cand_auth@test.com");
        recruiter = createRecruiter("cand_rec_auth@test.com");
        candidateToken = generateToken("cand_auth@test.com", "CANDIDATE");
        recruiterToken = generateToken("cand_rec_auth@test.com", "RECRUITER");
        offer = offerRepository.save(Offer.builder()
                .title("Test Offer")
                .description("Test")
                .recruiter(recruiter)
                .build());
    }

    @Test
    @DisplayName("GET /candidacy/candidacies should return empty list initially")
    void getAllCandidacies_whenEmpty_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/candidacy/candidacies",
                HttpMethod.GET,
                authHeader(candidateToken),
                List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("POST /candidacy should create a candidacy")
    void addCandidacy_shouldReturn201() {
        Candidacy candidacy = new Candidacy();
        candidacy.setCandidate(candidate);
        candidacy.setOffer(offer);
        candidacy.setStatus(Status.PENDING);
        candidacy.setSubmissionDate(new Date());

        ResponseEntity<Candidacy> response = restTemplate.exchange(
                "/candidacy",
                HttpMethod.POST,
                authEntity(candidacy, candidateToken),
                Candidacy.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    @DisplayName("GET /candidacy/{id} should return candidacy when found")
    void getCandidacyById_whenFound_shouldReturn200() {
        Candidacy saved = candidacyRepository.save(Candidacy.builder()
                .candidate(candidate)
                .offer(offer)
                .status(Status.PENDING)
                .submissionDate(new Date())
                .build());

        ResponseEntity<Candidacy> response = restTemplate.exchange(
                "/candidacy/" + saved.getId(),
                HttpMethod.GET,
                authHeader(candidateToken),
                Candidacy.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("GET /candidacy/{id} should return 404 when not found")
    void getCandidacyById_whenNotFound_shouldReturn404() {
        ResponseEntity<Candidacy> response = restTemplate.exchange(
                "/candidacy/9999",
                HttpMethod.GET,
                authHeader(candidateToken),
                Candidacy.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /candidacy/{id}/accept should accept candidacy")
    void acceptCandidacy_shouldReturn204() {
        Candidacy saved = candidacyRepository.save(Candidacy.builder()
                .candidate(candidate)
                .offer(offer)
                .status(Status.PENDING)
                .submissionDate(new Date())
                .build());

        ResponseEntity<Void> response = restTemplate.exchange(
                "/candidacy/" + saved.getId() + "/accept",
                HttpMethod.PUT,
                authHeader(recruiterToken),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Candidacy updated = candidacyRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Status.ACCEPTED);
    }

    @Test
    @DisplayName("PUT /candidacy/{id}/decline should decline candidacy")
    void declineCandidacy_shouldReturn204() {
        Candidacy saved = candidacyRepository.save(Candidacy.builder()
                .candidate(candidate)
                .offer(offer)
                .status(Status.PENDING)
                .submissionDate(new Date())
                .build());

        ResponseEntity<Void> response = restTemplate.exchange(
                "/candidacy/" + saved.getId() + "/decline",
                HttpMethod.PUT,
                authHeader(recruiterToken),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Candidacy updated = candidacyRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Status.DECLINED);
    }

    @Test
    @DisplayName("GET /candidacy/candidacies/exists should check candidacy existence")
    void existsCandidacy_shouldReturnBoolean() {
        candidacyRepository.save(Candidacy.builder()
                .candidate(candidate)
                .offer(offer)
                .status(Status.PENDING)
                .submissionDate(new Date())
                .build());

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/candidacy/candidacies/exists?offerId=" + offer.getId() + "&candidateId=" + candidate.getId(),
                HttpMethod.GET,
                authHeader(candidateToken),
                Boolean.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isTrue();
    }

    @Test
    @DisplayName("DELETE /candidacy/{id} should delete candidacy")
    void deleteCandidacy_shouldReturn200() {
        Candidacy saved = candidacyRepository.save(Candidacy.builder()
                .candidate(candidate)
                .offer(offer)
                .status(Status.PENDING)
                .submissionDate(new Date())
                .build());

        ResponseEntity<String> response = restTemplate.exchange(
                "/candidacy/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(candidateToken),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(candidacyRepository.findById(saved.getId())).isEmpty();
    }
}
