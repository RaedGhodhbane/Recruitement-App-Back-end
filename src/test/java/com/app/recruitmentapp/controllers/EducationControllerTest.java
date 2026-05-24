package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.EducationDTO;
import com.app.recruitmentapp.services.EducationService;
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
@DisplayName("EducationController Unit Tests")
class EducationControllerTest {

    @Mock
    private EducationService educationService;

    @InjectMocks
    private EducationController educationController;

    private EducationDTO testEducationDTO;

    @BeforeEach
    void setUp() {
        testEducationDTO = new EducationDTO();
        testEducationDTO.setId(1L);
        testEducationDTO.setDiploma("Master");
        testEducationDTO.setUniversity("University");
    }

    @Nested
    @DisplayName("GET /education/educations")
    class GetAllEducationsTests {

        @Test
        @DisplayName("Should return all educations")
        void shouldReturnAllEducations() {
            when(educationService.getAllEducations()).thenReturn(List.of(testEducationDTO));

            List<EducationDTO> result = educationController.getAllEducations();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /education/{id}")
    class GetEducationByIdTests {

        @Test
        @DisplayName("Should return education when found")
        void shouldReturnEducationWhenFound() {
            when(educationService.getEducationById(1L)).thenReturn(Optional.of(testEducationDTO));

            ResponseEntity<EducationDTO> response = educationController.getEducationById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(educationService.getEducationById(99L)).thenReturn(Optional.empty());

            ResponseEntity<EducationDTO> response = educationController.getEducationById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /education/{idCandidate}")
    class AddEducationTests {

        @Test
        @DisplayName("Should add education successfully")
        void shouldAddEducationSuccessfully() {
            when(educationService.saveEducation(testEducationDTO, 1L)).thenReturn(testEducationDTO);

            ResponseEntity<EducationDTO> response = educationController.addEducation(testEducationDTO, 1L);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /education/{id}")
    class UpdateEducationTests {

        @Test
        @DisplayName("Should update education successfully")
        void shouldUpdateEducationSuccessfully() {
            when(educationService.updateEducation(1L, testEducationDTO)).thenReturn(testEducationDTO);

            ResponseEntity<EducationDTO> response = educationController.updateEducation(1L, testEducationDTO);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(educationService.updateEducation(99L, testEducationDTO)).thenThrow(new RuntimeException("Education not found"));

            ResponseEntity<EducationDTO> response = educationController.updateEducation(99L, testEducationDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /education/{id}")
    class DeleteEducationTests {

        @Test
        @DisplayName("Should delete education successfully")
        void shouldDeleteEducationSuccessfully() {
            doNothing().when(educationService).deleteEducation(1L);

            ResponseEntity<Map<String, String>> response = educationController.deleteEducation(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Education deleted successfully", response.getBody().get("message"));
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Education not found")).when(educationService).deleteEducation(99L);

            ResponseEntity<Map<String, String>> response = educationController.deleteEducation(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }
}
