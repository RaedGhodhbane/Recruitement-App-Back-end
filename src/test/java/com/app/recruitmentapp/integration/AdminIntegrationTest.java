package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Admin API - Integration Tests")
class AdminIntegrationTest extends AbstractIntegrationTest {

    private String adminToken;
    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = createAdmin("admin@test.com");
        adminToken = generateToken("admin@test.com", "ADMIN");
    }

    @Test
    @DisplayName("GET /admin/admins should return all admins")
    void getAllAdmins_shouldReturnList() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/admin/admins", HttpMethod.GET, authHeader(adminToken), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("GET /admin/{id} should return admin when found")
    void getAdminById_whenFound_shouldReturn200() {
        ResponseEntity<Admin> response = restTemplate.exchange(
                "/admin/" + admin.getId(), HttpMethod.GET, authHeader(adminToken), Admin.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("admin@test.com");
    }

    @Test
    @DisplayName("GET /admin/{id} should return 404 when not found")
    void getAdminById_whenNotFound_shouldReturn404() {
        ResponseEntity<Admin> response = restTemplate.exchange(
                "/admin/9999", HttpMethod.GET, authHeader(adminToken), Admin.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE /admin/{id} should delete admin")
    void deleteAdmin_shouldReturn200() {
        Admin toDelete = createAdmin("delete_admin@test.com");

        ResponseEntity<String> response = restTemplate.exchange(
                "/admin/" + toDelete.getId(),
                HttpMethod.DELETE,
                authHeader(adminToken),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
        assertThat(adminRepository.findById(toDelete.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /admin/{id} should return 404 when not found")
    void deleteAdmin_whenNotFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/admin/9999", HttpMethod.DELETE, authHeader(adminToken), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /admin/recruiters/{id}/activate should activate recruiter")
    void activateRecruiter_shouldReturn200() {
        Recruiter recruiter = createRecruiter("activate_recruiter@test.com");
        recruiter.setActive(false);
        recruiterRepository.save(recruiter);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/admin/recruiters/" + recruiter.getId() + "/activate",
                HttpMethod.PUT,
                authHeader(adminToken),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Recruiter updated = recruiterRepository.findById(recruiter.getId()).orElseThrow();
        assertThat(updated.getActive()).isTrue();
    }

    @Test
    @DisplayName("PUT /admin/recruiters/{id}/desactivate should deactivate recruiter")
    void desactivateRecruiter_shouldReturn200() {
        Recruiter recruiter = createRecruiter("deactivate_recruiter@test.com");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/admin/recruiters/" + recruiter.getId() + "/desactivate",
                HttpMethod.PUT,
                authHeader(adminToken),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Recruiter updated = recruiterRepository.findById(recruiter.getId()).orElseThrow();
        assertThat(updated.getActive()).isFalse();
    }

    @Test
    @DisplayName("PUT /admin/candidates/{id}/activate should activate candidate")
    void activateCandidate_shouldReturn200() {
        Candidate candidate = createCandidate("activate_cand@test.com");
        candidate.setActive(false);
        candidateRepository.save(candidate);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/admin/candidates/" + candidate.getId() + "/activate",
                HttpMethod.PUT,
                authHeader(adminToken),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Candidate updated = candidateRepository.findById(candidate.getId()).orElseThrow();
        assertThat(updated.getActive()).isTrue();
    }

    @Test
    @DisplayName("PUT /admin/candidates/{id}/desactivate should deactivate candidate")
    void desactivateCandidate_shouldReturn200() {
        Candidate candidate = createCandidate("deactivate_cand@test.com");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/admin/candidates/" + candidate.getId() + "/desactivate",
                HttpMethod.PUT,
                authHeader(adminToken),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Candidate updated = candidateRepository.findById(candidate.getId()).orElseThrow();
        assertThat(updated.getActive()).isFalse();
    }

    @Test
    @DisplayName("PUT /admin/{id}/change-password should change password")
    void changePassword_shouldReturn200() {
        Map<String, String> body = Map.of(
                "currentPassword", "password123",
                "newPassword", "newPass456",
                "confirmPassword", "newPass456"
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                "/admin/" + admin.getId() + "/change-password",
                HttpMethod.PUT,
                authEntity(body, adminToken),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("PUT /admin/{id}/change-password should return 400 with wrong old password")
    void changePassword_withWrongOldPassword_shouldReturn400() {
        Map<String, String> body = Map.of(
                "currentPassword", "wrong",
                "newPassword", "newPass456",
                "confirmPassword", "newPass456"
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                "/admin/" + admin.getId() + "/change-password",
                HttpMethod.PUT,
                authEntity(body, adminToken),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("GET /admin/contact/messages should return contact messages")
    void getAllMessagesContact_shouldReturnList() {
        contactRepository.save(Contact.builder()
                .name("John")
                .subject("Help")
                .email("john@test.com")
                .message("Need help")
                .build());

        ResponseEntity<List> response = restTemplate.exchange(
                "/admin/contact/messages", HttpMethod.GET, authHeader(adminToken), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}
