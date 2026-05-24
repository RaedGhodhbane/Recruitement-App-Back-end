package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.ExperienceDTO;
import com.app.recruitmentapp.services.ExperienceService;
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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExperienceController Unit Tests")
class ExperienceControllerTest {

    @Mock
    private ExperienceService experienceService;

    @InjectMocks
    private ExperienceController experienceController;

    private ExperienceDTO testExperienceDTO;

    @BeforeEach
    void setUp() {
        testExperienceDTO = new ExperienceDTO();
        testExperienceDTO.setId(1L);
        testExperienceDTO.setJobTitle("Software Engineer");
        testExperienceDTO.setCompanyName("Tech Corp");
    }

    @Nested
    @DisplayName("GET /experience/experiences")
    class GetAllExperiencesTests {

        @Test
        @DisplayName("Should return all experiences")
        void shouldReturnAllExperiences() {
            when(experienceService.getAllExperiences()).thenReturn(List.of(testExperienceDTO));

            List<ExperienceDTO> result = experienceController.getAllExperiences();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /experience/{id}")
    class GetExperienceByIdTests {

        @Test
        @DisplayName("Should return experience when found")
        void shouldReturnExperienceWhenFound() {
            when(experienceService.getExperienceById(1L)).thenReturn(Optional.of(testExperienceDTO));

            ResponseEntity<ExperienceDTO> response = experienceController.getExperienceById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(experienceService.getExperienceById(99L)).thenReturn(Optional.empty());

            ResponseEntity<ExperienceDTO> response = experienceController.getExperienceById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /experience/{idCandidate}")
    class AddExperienceTests {

        @Test
        @DisplayName("Should add experience successfully")
        void shouldAddExperienceSuccessfully() {
            when(experienceService.saveExperience(testExperienceDTO, 1L)).thenReturn(testExperienceDTO);

            ResponseEntity<ExperienceDTO> response = experienceController.addExperience(testExperienceDTO, 1L);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /experience/{id}")
    class UpdateExperienceTests {

        @Test
        @DisplayName("Should update experience successfully")
        void shouldUpdateExperienceSuccessfully() {
            when(experienceService.updateExperience(1L, testExperienceDTO)).thenReturn(testExperienceDTO);

            ResponseEntity<ExperienceDTO> response = experienceController.updateExperience(1L, testExperienceDTO);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(experienceService.updateExperience(99L, testExperienceDTO)).thenThrow(new RuntimeException("Experience not found"));

            ResponseEntity<ExperienceDTO> response = experienceController.updateExperience(99L, testExperienceDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /experience/{id}")
    class DeleteExperienceTests {

        @Test
        @DisplayName("Should delete experience successfully")
        void shouldDeleteExperienceSuccessfully() {
            doNothing().when(experienceService).deleteExperience(1L);

            ResponseEntity<Map<String, String>> response = experienceController.deleteExperience(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Experience deleted successfully", response.getBody().get("message"));
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Experience not found")).when(experienceService).deleteExperience(99L);

            ResponseEntity<Map<String, String>> response = experienceController.deleteExperience(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }
}
