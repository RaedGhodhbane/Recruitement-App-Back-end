package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User API - Integration Tests")
class UserIntegrationTest extends AbstractIntegrationTest {

    private String adminToken;

    @BeforeEach
    void setUp() {
        createAdmin("user_admin@test.com");
        adminToken = generateToken("user_admin@test.com", "ADMIN");
    }

    @Test
    @DisplayName("GET /admin/user/users should return all users")
    void getAllUsers_shouldReturnList() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/admin/user/users", HttpMethod.GET, authHeader(adminToken), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("POST /admin/user should create a user")
    void addUser_shouldReturn201() {
        User user = new User();
        user.setEmail("newuser@test.com");
        user.setPassword("password123");
        user.setName("New");
        user.setFirstName("User");
        user.setRole(Role.CANDIDATE);

        ResponseEntity<User> response = restTemplate.exchange(
                "/admin/user",
                HttpMethod.POST,
                authEntity(user, adminToken),
                User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("newuser@test.com");
    }

    @Test
    @DisplayName("GET /admin/user/{id} should return user when found")
    void getUserById_whenFound_shouldReturn200() {
        User saved = userRepository.findAll().stream().findFirst().orElseThrow();

        ResponseEntity<User> response = restTemplate.exchange(
                "/admin/user/" + saved.getId(),
                HttpMethod.GET,
                authHeader(adminToken),
                User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo(saved.getEmail());
    }

    @Test
    @DisplayName("GET /admin/user/{id} should return 404 when not found")
    void getUserById_whenNotFound_shouldReturn404() {
        ResponseEntity<User> response = restTemplate.exchange(
                "/admin/user/9999", HttpMethod.GET, authHeader(adminToken), User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /admin/user/{id} should update user")
    void updateUser_shouldReturn200() {
        User saved = userRepository.findAll().stream().findFirst().orElseThrow();

        User updates = new User();
        updates.setName("Updated Name");

        ResponseEntity<User> response = restTemplate.exchange(
                "/admin/user/" + saved.getId(),
                HttpMethod.PUT,
                authEntity(updates, adminToken),
                User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
    }

    @Test
    @DisplayName("PUT /admin/user/{id} should return 404 when not found")
    void updateUser_whenNotFound_shouldReturn404() {
        User updates = new User();
        updates.setName("No matter");

        ResponseEntity<User> response = restTemplate.exchange(
                "/admin/user/9999", HttpMethod.PUT, authEntity(updates, adminToken), User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE /admin/user/{id} should delete user")
    void deleteUser_shouldReturn200() {
        User user = new User();
        user.setEmail("delete_user@test.com");
        user.setPassword("pass");
        user.setName("Delete");
        user.setFirstName("Me");
        user.setRole(Role.CANDIDATE);
        User saved = userRepository.save(user);

        ResponseEntity<String> response = restTemplate.exchange(
                "/admin/user/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(adminToken),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /admin/user/{id} should return 404 when not found")
    void deleteUser_whenNotFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/admin/user/9999", HttpMethod.DELETE, authHeader(adminToken), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
