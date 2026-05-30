package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Recruiter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Offer API - Integration Tests")
class OfferIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("GET /offer/offers should return empty list initially")
    void getAllOffers_whenEmpty_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "/offer/offers", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("GET /offer/offers should return all offers")
    void getAllOffers_shouldReturnList() {
        Recruiter recruiter = createRecruiter("offer_recruiter@test.com");
        offerRepository.save(Offer.builder()
                .title("Dev Java")
                .description("Poste de développeur Java")
                .recruiter(recruiter)
                .build());
        offerRepository.save(Offer.builder()
                .title("Dev Angular")
                .description("Poste de développeur Angular")
                .recruiter(recruiter)
                .build());

        ResponseEntity<List> response = restTemplate.getForEntity(
                "/offer/offers", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("GET /offer/{id} should return offer when found")
    void getOfferById_whenFound_shouldReturn200() {
        Recruiter recruiter = createRecruiter("offer_found@test.com");
        Offer saved = offerRepository.save(Offer.builder()
                .title("Data Scientist")
                .description("Poste en data science")
                .recruiter(recruiter)
                .build());

        ResponseEntity<Offer> response = restTemplate.getForEntity(
                "/offer/" + saved.getId(), Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Data Scientist");
    }

    @Test
    @DisplayName("GET /offer/{id} should return 404 when not found")
    void getOfferById_whenNotFound_shouldReturn404() {
        ResponseEntity<Offer> response = restTemplate.getForEntity(
                "/offer/9999", Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("POST /offer/{idRecruiter} should create offer with recruiter token")
    void addOfferWithRecruiterToken_shouldReturn201() {
        Recruiter recruiter = createRecruiter("add_offer@test.com");
        String token = generateToken("add_offer@test.com", "RECRUITER");

        Offer newOffer = new Offer();
        newOffer.setTitle("Full Stack");
        newOffer.setDescription("Poste full stack");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Offer> request = new HttpEntity<>(newOffer, headers);

        ResponseEntity<Offer> response = restTemplate.exchange(
                "/offer/" + recruiter.getId(),
                HttpMethod.POST,
                request,
                Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Full Stack");
    }

    @Test
    @DisplayName("PUT /offer/{id} should update offer")
    void updateOffer_shouldReturn200() {
        Recruiter recruiter = createRecruiter("update_offer@test.com");
        String token = generateToken("update_offer@test.com", "RECRUITER");
        Offer saved = offerRepository.save(Offer.builder()
                .title("Old Title")
                .description("Old description")
                .recruiter(recruiter)
                .build());

        Offer updates = new Offer();
        updates.setTitle("New Title");

        restTemplate.exchange("/offer/" + saved.getId(), HttpMethod.PUT,
                authEntity(updates, token), Offer.class);

        Offer updated = offerRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("New Title");
    }

    @Test
    @DisplayName("DELETE /offer/{id} should delete offer")
    void deleteOffer_shouldReturn200() {
        Recruiter recruiter = createRecruiter("delete_offer@test.com");
        String token = generateToken("delete_offer@test.com", "RECRUITER");
        Offer saved = offerRepository.save(Offer.builder()
                .title("To Delete")
                .description("Will be deleted")
                .recruiter(recruiter)
                .build());

        ResponseEntity<String> response = restTemplate.exchange(
                "/offer/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(token),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
        assertThat(offerRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /offer/{id} should return 404 when not found")
    void deleteOffer_whenNotFound_shouldReturn404() {
        Recruiter recruiter = createRecruiter("notfound_offer@test.com");
        String token = generateToken("notfound_offer@test.com", "RECRUITER");

        ResponseEntity<String> response = restTemplate.exchange(
                "/offer/9999",
                HttpMethod.DELETE,
                authHeader(token),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
