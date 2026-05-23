package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Candidacy;
import com.app.recruitmentapp.services.CandidacyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CandidacyController Unit Tests")
class CandidacyControllerTest {

    @Mock
    private CandidacyService candidacyService;

    @InjectMocks
    private CandidacyController candidacyController;

    private Candidacy testCandidacy;

    @BeforeEach
    void setUp() {
        testCandidacy = new Candidacy();
        testCandidacy.setId(1L);
    }

    @Nested
    @DisplayName("GET /candidacy/candidacies")
    class GetAllCandidaciesTests {

        @Test
        @DisplayName("Should return all candidacies")
        void shouldReturnAllCandidacies() {
            when(candidacyService.getAllCandidacies()).thenReturn(List.of(testCandidacy));

            List<Candidacy> result = candidacyController.getAllCandidacies();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /candidacy/{id}")
    class GetCandidacyByIdTests {

        @Test
        @DisplayName("Should return candidacy when found")
        void shouldReturnCandidacyWhenFound() {
            when(candidacyService.getCandidacyById(1L)).thenReturn(Optional.of(testCandidacy));

            ResponseEntity<Candidacy> response = candidacyController.getCandidacyById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(candidacyService.getCandidacyById(99L)).thenReturn(Optional.empty());

            ResponseEntity<Candidacy> response = candidacyController.getCandidacyById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /candidacy")
    class AddCandidacyTests {

        @Test
        @DisplayName("Should add candidacy successfully")
        void shouldAddCandidacySuccessfully() {
            when(candidacyService.saveCandidacy(testCandidacy)).thenReturn(testCandidacy);

            ResponseEntity<Candidacy> response = candidacyController.addCandidacy(testCandidacy);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /candidacy/{id}")
    class UpdateCandidacyTests {

        @Test
        @DisplayName("Should update candidacy successfully")
        void shouldUpdateCandidacySuccessfully() {
            when(candidacyService.updateCandidacy(1L, testCandidacy)).thenReturn(testCandidacy);

            ResponseEntity<Candidacy> response = candidacyController.updateCandidacy(1L, testCandidacy);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(candidacyService.updateCandidacy(99L, testCandidacy)).thenThrow(new RuntimeException("Candidacy not found"));

            ResponseEntity<Candidacy> response = candidacyController.updateCandidacy(99L, testCandidacy);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /candidacy/{id}")
    class DeleteCandidacyTests {

        @Test
        @DisplayName("Should delete candidacy successfully")
        void shouldDeleteCandidacySuccessfully() {
            doNothing().when(candidacyService).deleteCandidacy(1L);

            ResponseEntity<String> response = candidacyController.deleteCandidacy(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Candidacy deleted successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Candidacy not found")).when(candidacyService).deleteCandidacy(99L);

            ResponseEntity<String> response = candidacyController.deleteCandidacy(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /candidacy/{id}/accept")
    class AcceptApplicationTests {

        @Test
        @DisplayName("Should accept application successfully")
        void shouldAcceptApplicationSuccessfully() {
            doNothing().when(candidacyService).acceptApplication(1L);

            ResponseEntity<Void> response = candidacyController.acceptApplication(1L);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /candidacy/{id}/decline")
    class DeclineApplicationTests {

        @Test
        @DisplayName("Should decline application successfully")
        void shouldDeclineApplicationSuccessfully() {
            doNothing().when(candidacyService).declineApplication(1L);

            ResponseEntity<Void> response = candidacyController.declineApplication(1L);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /candidacy/candidacies/exists")
    class ExistsCandidacyTests {

        @Test
        @DisplayName("Should return true when candidacy exists")
        void shouldReturnTrueWhenExists() {
            when(candidacyService.candidacyExists(1L, 1L)).thenReturn(true);

            boolean result = candidacyController.existsCandidacy(1L, 1L);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when candidacy does not exist")
        void shouldReturnFalseWhenNotExists() {
            when(candidacyService.candidacyExists(1L, 1L)).thenReturn(false);

            boolean result = candidacyController.existsCandidacy(1L, 1L);

            assertFalse(result);
        }
    }
}
