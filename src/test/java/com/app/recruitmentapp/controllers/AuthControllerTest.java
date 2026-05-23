package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.security.JwtUtil;
import com.app.recruitmentapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Nested
    @DisplayName("POST /api/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully")
        void shouldLoginSuccessfully() {
            Map<String, String> request = Map.of("email", "user@test.com", "password", "password123");
            Map<String, Object> expectedResult = Map.of("token", "jwt-token", "role", "CANDIDATE");

            when(userService.login("user@test.com", "password123")).thenReturn(expectedResult);

            ResponseEntity<?> response = authController.login(request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Should return 401 when login fails")
        void shouldReturn401WhenLoginFails() {
            Map<String, String> request = Map.of("email", "wrong@test.com", "password", "wrongpass");

            when(userService.login("wrong@test.com", "wrongpass")).thenThrow(new RuntimeException("Invalid credentials"));

            ResponseEntity<?> response = authController.login(request);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("Invalid credentials"));
        }
    }

    @Nested
    @DisplayName("POST /api/auth/logout")
    class LogoutTests {

        @Test
        @DisplayName("Should logout successfully when token is present")
        void shouldLogoutSuccessfullyWhenTokenPresent() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(jwtUtil.extractToken(request)).thenReturn("valid-token");

            ResponseEntity<String> response = authController.logout(request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(userService).logout("valid-token");
        }

        @Test
        @DisplayName("Should return 400 when token is missing")
        void shouldReturn400WhenTokenMissing() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(jwtUtil.extractToken(request)).thenReturn(null);

            ResponseEntity<String> response = authController.logout(request);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Token manquant", response.getBody());
        }
    }
}
