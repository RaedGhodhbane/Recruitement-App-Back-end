package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Candidate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Auth API - Integration Tests")
class AuthIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("POST /api/auth/login should return 401 for unknown user")
    void loginWithUnknownUser_shouldReturn401() {
        Map<String, String> body = Map.of("email", "unknown@test.com", "password", "wrongpass");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/auth/login", body, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("POST /api/auth/login should return 200 with token for valid candidate")
    void loginWithValidCandidate_shouldReturn200() {
        createCandidate("candidate@test.com");
        Map<String, String> body = Map.of("email", "candidate@test.com", "password", "password123");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/auth/login", body, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("token");
        assertThat(response.getBody()).containsKey("user");
    }

    @Test
    @DisplayName("POST /api/auth/login should return 401 for disabled candidate")
    void loginWithDisabledCandidate_shouldReturn401() {
        Candidate c = createCandidate("disabled@test.com");
        c.setActive(false);
        candidateRepository.save(c);

        Map<String, String> body = Map.of("email", "disabled@test.com", "password", "password123");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/auth/login", body, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("POST /api/auth/login should return 200 with token for valid recruiter")
    void loginWithValidRecruiter_shouldReturn200() {
        createRecruiter("recruiter@test.com");
        Map<String, String> body = Map.of("email", "recruiter@test.com", "password", "password123");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/auth/login", body, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("token");
    }

    @Test
    @DisplayName("POST /api/auth/logout should return 200 when token is provided")
    void logoutWithToken_shouldReturn200() {
        createRecruiter("logout@test.com");
        Map<String, String> body = Map.of("email", "logout@test.com", "password", "password123");
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity("/api/auth/login", body, Map.class);
        String token = (String) loginResponse.getBody().get("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> logoutResponse = restTemplate.exchange(
                "/api/auth/logout",
                HttpMethod.POST,
                request,
                String.class);

        assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("POST /api/auth/logout should return 400 without token")
    void logoutWithoutToken_shouldReturn400() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/logout", null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
