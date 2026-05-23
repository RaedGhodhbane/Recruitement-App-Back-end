package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Favourite (Saved Jobs) API - Integration Tests")
class FavouriteIntegrationTest extends AbstractIntegrationTest {

    private String token;
    private Candidate candidate;
    private Recruiter recruiter;
    private Offer offer;

    @BeforeEach
    void setUp() {
        candidate = createCandidate("fav_candidate@test.com");
        token = generateToken("fav_candidate@test.com", "CANDIDATE");
        recruiter = createRecruiter("fav_recruiter@test.com");
        offer = offerRepository.save(Offer.builder()
                .title("Fav Job")
                .description("Job to save")
                .recruiter(recruiter)
                .build());
    }

    @Test
    @DisplayName("POST /api/saved-jobs/{userId}/{offerId} should save a job")
    void saveJob_shouldReturnFavourite() {
        ResponseEntity<Favourite> response = restTemplate.exchange(
                "/api/saved-jobs/" + candidate.getId() + "/" + offer.getId(),
                HttpMethod.POST,
                authHeader(token),
                Favourite.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getOffer().getId()).isEqualTo(offer.getId());
    }

    @Test
    @DisplayName("GET /api/saved-jobs/{userId} should return saved jobs")
    void getSavedJobs_shouldReturnList() {
        favouriteRepository.save(new Favourite(null, candidate, offer));

        ResponseEntity<List> response = restTemplate.exchange(
                "/api/saved-jobs/" + candidate.getId(),
                HttpMethod.GET,
                authHeader(token),
                List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("DELETE /api/saved-jobs/{savedJobId} should remove saved job")
    void removeSavedJob_shouldReturn200() {
        Favourite saved = favouriteRepository.save(new Favourite(null, candidate, offer));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/saved-jobs/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(token),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(favouriteRepository.findById(saved.getId())).isEmpty();
    }
}
