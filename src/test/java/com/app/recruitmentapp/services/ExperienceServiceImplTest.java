package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Experience;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExperienceServiceImpl Unit Tests")
class ExperienceServiceImplTest {

    @Mock
    private ExperienceRepository experienceRepository;
    @Mock
    private CandidateRepository candidateRepository;
    @InjectMocks
    private ExperienceServiceImpl experienceService;

    private Experience experience1;
    private Experience experience2;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = new Candidate();
        candidate.setId(1L);
        candidate.setEmail("candidate@test.com");

        experience1 = new Experience();
        experience1.setId(1L);
        experience1.setCompanyName("Google");
        experience1.setJobTitle("Software Engineer");
        experience1.setStartExpDate(Date.valueOf("2022-01-01"));
        experience1.setEndExpDate(Date.valueOf("2024-06-30"));
        experience1.setDescription("Développement backend");
        experience1.setCandidate(candidate);

        experience2 = new Experience();
        experience2.setId(2L);
        experience2.setCompanyName("Microsoft");
        experience2.setJobTitle("Intern");
        experience2.setStartExpDate(Date.valueOf("2021-06-01"));
        experience2.setEndExpDate(Date.valueOf("2021-12-31"));
        experience2.setDescription("Stage été");
        experience2.setCandidate(candidate);
    }

    @Nested
    @DisplayName("getAllExperiences Tests")
    class GetAllExperiencesTests {

        @Test
        @DisplayName("Should return all experiences")
        void shouldGetAllExperiencesSuccessfully() {
            List<Experience> mockExperiences = List.of(experience1, experience2);
            when(experienceRepository.findAll()).thenReturn(mockExperiences);

            List<Experience> result = experienceService.getAllExperiences();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Google", result.get(0).getCompanyName());
            assertEquals("Microsoft", result.get(1).getCompanyName());
            verify(experienceRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getExperienceById Tests")
    class GetExperienceByIdTests {

        @Test
        @DisplayName("Should return experience when found")
        void shouldReturnExperienceWhenFound() {
            when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience1));

            Optional<Experience> result = experienceService.getExperienceById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("Software Engineer", result.get().getJobTitle());
            verify(experienceRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(experienceRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Experience> result = experienceService.getExperienceById(99L);

            assertFalse(result.isPresent());
            verify(experienceRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("saveExperience Tests")
    class SaveExperienceTests {

        @Test
        @DisplayName("Should save experience successfully")
        void shouldSaveExperienceSuccessfully() {
            Experience newExperience = new Experience();
            newExperience.setCompanyName("Amazon");
            newExperience.setJobTitle("SDE");
            newExperience.setStartExpDate(Date.valueOf("2023-01-01"));
            newExperience.setEndExpDate(Date.valueOf("2025-06-30"));
            newExperience.setDescription("Cloud computing");

            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
            when(experienceRepository.save(any(Experience.class))).thenAnswer(invocation -> {
                Experience saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Experience result = experienceService.saveExperience(newExperience, 1L);

            assertNotNull(result);
            assertEquals("Amazon", result.getCompanyName());
            assertEquals("SDE", result.getJobTitle());
            assertEquals(candidate, result.getCandidate());
            verify(candidateRepository).findById(1L);
            verify(experienceRepository).save(any(Experience.class));
        }

        @Test
        @DisplayName("Should save experience with null candidate when candidate not found")
        void shouldSaveExperienceWithNullCandidateWhenNotFound() {
            Experience newExperience = new Experience();
            newExperience.setCompanyName("Amazon");
            newExperience.setJobTitle("SDE");

            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());
            when(experienceRepository.save(any(Experience.class))).thenAnswer(invocation -> {
                Experience saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Experience result = experienceService.saveExperience(newExperience, 99L);

            assertNotNull(result);
            assertNull(result.getCandidate());
            verify(candidateRepository).findById(99L);
            verify(experienceRepository).save(any(Experience.class));
        }
    }

    @Nested
    @DisplayName("updateExperience Tests")
    class UpdateExperienceTests {

        @Test
        @DisplayName("Should update experience when found")
        void shouldUpdateExperienceWhenFound() {
            Experience updatedData = new Experience();
            updatedData.setCompanyName("Apple");
            updatedData.setJobTitle("iOS Developer");
            updatedData.setStartExpDate(Date.valueOf("2023-03-01"));
            updatedData.setEndExpDate(Date.valueOf("2025-09-30"));
            updatedData.setDescription("Développement iOS");

            when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience1));
            when(experienceRepository.saveAndFlush(experience1)).thenReturn(experience1);

            Experience result = experienceService.updateExperience(1L, updatedData);

            assertNotNull(result);
            assertEquals("Apple", result.getCompanyName());
            assertEquals("iOS Developer", result.getJobTitle());
            assertEquals(Date.valueOf("2023-03-01"), result.getStartExpDate());
            assertEquals(Date.valueOf("2025-09-30"), result.getEndExpDate());
            assertEquals("Développement iOS", result.getDescription());
            verify(experienceRepository).findById(1L);
            verify(experienceRepository).saveAndFlush(experience1);
        }

        @Test
        @DisplayName("Should throw NullPointerException when experience not found")
        void shouldThrowWhenExperienceNotFound() {
            Experience updatedData = new Experience();
            updatedData.setCompanyName("Apple");

            when(experienceRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(NullPointerException.class,
                    () -> experienceService.updateExperience(99L, updatedData));
            verify(experienceRepository).findById(99L);
            verify(experienceRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("deleteExperience Tests")
    class DeleteExperienceTests {

        @Test
        @DisplayName("Should delete experience when found")
        void shouldDeleteExperienceWhenFound() {
            when(experienceRepository.existsById(1L)).thenReturn(true);

            experienceService.deleteExperience(1L);

            verify(experienceRepository).existsById(1L);
            verify(experienceRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenExperienceNotFound() {
            when(experienceRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> experienceService.deleteExperience(99L));
            verify(experienceRepository).existsById(99L);
            verify(experienceRepository, never()).deleteById(anyLong());
        }
    }
}
