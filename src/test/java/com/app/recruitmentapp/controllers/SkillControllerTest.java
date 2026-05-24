package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.SkillDTO;
import com.app.recruitmentapp.services.SkillService;
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
@DisplayName("SkillController Unit Tests")
class SkillControllerTest {

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillController skillController;

    private SkillDTO testSkillDTO;

    @BeforeEach
    void setUp() {
        testSkillDTO = new SkillDTO();
        testSkillDTO.setId(1L);
        testSkillDTO.setTitle("Java");
    }

    @Nested
    @DisplayName("GET /skill/skills")
    class GetAllSkillsTests {

        @Test
        @DisplayName("Should return all skills")
        void shouldReturnAllSkills() {
            when(skillService.getAllSkills()).thenReturn(List.of(testSkillDTO));

            List<SkillDTO> result = skillController.getAllSkills();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /skill/{id}")
    class GetSkillByIdTests {

        @Test
        @DisplayName("Should return skill when found")
        void shouldReturnSkillWhenFound() {
            when(skillService.getSkillById(1L)).thenReturn(Optional.of(testSkillDTO));

            ResponseEntity<SkillDTO> response = skillController.getSkillById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(skillService.getSkillById(99L)).thenReturn(Optional.empty());

            ResponseEntity<SkillDTO> response = skillController.getSkillById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /skill/{idCandidate}")
    class AddSkillTests {

        @Test
        @DisplayName("Should add skill successfully")
        void shouldAddSkillSuccessfully() {
            when(skillService.saveSkill(testSkillDTO, 1L)).thenReturn(testSkillDTO);

            ResponseEntity<SkillDTO> response = skillController.addSkill(testSkillDTO, 1L);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /skill/{id}")
    class UpdateSkillTests {

        @Test
        @DisplayName("Should update skill successfully")
        void shouldUpdateSkillSuccessfully() {
            when(skillService.updateSkill(1L, testSkillDTO)).thenReturn(testSkillDTO);

            ResponseEntity<SkillDTO> response = skillController.updateSkill(1L, testSkillDTO);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(skillService.updateSkill(99L, testSkillDTO)).thenThrow(new RuntimeException("Skill not found"));

            ResponseEntity<SkillDTO> response = skillController.updateSkill(99L, testSkillDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /skill/{id}")
    class DeleteSkillTests {

        @Test
        @DisplayName("Should delete skill successfully")
        void shouldDeleteSkillSuccessfully() {
            doNothing().when(skillService).deleteSkill(1L);

            ResponseEntity<Map<String, String>> response = skillController.deleteSkill(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Skill deleted successfully", response.getBody().get("message"));
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Skill not found")).when(skillService).deleteSkill(99L);

            ResponseEntity<Map<String, String>> response = skillController.deleteSkill(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }
}
