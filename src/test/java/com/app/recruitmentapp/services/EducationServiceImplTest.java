package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Education;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.EducationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EducationServiceImpl Unit Tests")
class EducationServiceImplTest {

    @Mock
    private EducationRepository educationRepository;
    @Mock
    private CandidateRepository candidateRepository;
    @InjectMocks
    private EducationServiceImpl educationService;

    private Education education1;
    private Education education2;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = new Candidate();
        candidate.setId(1L);
        candidate.setEmail("candidate@test.com");

        education1 = new Education();
        education1.setId(1L);
        education1.setDiploma("Master");
        education1.setUniversity("Sorbonne");
        education1.setEndDate("2024-06-30");
        education1.setDescription("Master en informatique");
        education1.setCandidate(candidate);

        education2 = new Education();
        education2.setId(2L);
        education2.setDiploma("Bachelor");
        education2.setUniversity("Tunis");
        education2.setEndDate("2022-06-30");
        education2.setDescription("Licence en informatique");
        education2.setCandidate(candidate);
    }

    @Nested
    @DisplayName("getAllEducations Tests")
    class GetAllEducationsTests {

        @Test
        @DisplayName("Should return all educations")
        void shouldGetAllEducationsSuccessfully() {
            List<Education> mockEducations = List.of(education1, education2);
            when(educationRepository.findAll()).thenReturn(mockEducations);

            List<Education> result = educationService.getAllEducations();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Master", result.get(0).getDiploma());
            assertEquals("Bachelor", result.get(1).getDiploma());
            verify(educationRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getEducationById Tests")
    class GetEducationByIdTests {

        @Test
        @DisplayName("Should return education when found")
        void shouldReturnEducationWhenFound() {
            when(educationRepository.findById(1L)).thenReturn(Optional.of(education1));

            Optional<Education> result = educationService.getEducationById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("Sorbonne", result.get().getUniversity());
            verify(educationRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(educationRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Education> result = educationService.getEducationById(99L);

            assertFalse(result.isPresent());
            verify(educationRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("saveEducation Tests")
    class SaveEducationTests {

        @Test
        @DisplayName("Should save education successfully")
        void shouldSaveEducationSuccessfully() {
            Education newEducation = new Education();
            newEducation.setDiploma("PhD");
            newEducation.setUniversity("MIT");
            newEducation.setEndDate("2027-06-30");
            newEducation.setDescription("Doctorat en IA");

            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
            when(educationRepository.save(any(Education.class))).thenAnswer(invocation -> {
                Education saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Education result = educationService.saveEducation(newEducation, 1L);

            assertNotNull(result);
            assertEquals("PhD", result.getDiploma());
            assertEquals("MIT", result.getUniversity());
            assertEquals(candidate, result.getCandidate());
            verify(candidateRepository).findById(1L);
            verify(educationRepository).save(any(Education.class));
        }

        @Test
        @DisplayName("Should save education with null candidate when candidate not found")
        void shouldSaveEducationWithNullCandidateWhenNotFound() {
            Education newEducation = new Education();
            newEducation.setDiploma("PhD");
            newEducation.setUniversity("MIT");

            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());
            when(educationRepository.save(any(Education.class))).thenAnswer(invocation -> {
                Education saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Education result = educationService.saveEducation(newEducation, 99L);

            assertNotNull(result);
            assertNull(result.getCandidate());
            verify(candidateRepository).findById(99L);
            verify(educationRepository).save(any(Education.class));
        }
    }

    @Nested
    @DisplayName("updateEducation Tests")
    class UpdateEducationTests {

        @Test
        @DisplayName("Should update education when found")
        void shouldUpdateEducationWhenFound() {
            Education updatedData = new Education();
            updatedData.setDiploma("MBA");
            updatedData.setUniversity("HEC Paris");
            updatedData.setEndDate("2025-06-30");
            updatedData.setDescription("MBA en finance");

            when(educationRepository.findById(1L)).thenReturn(Optional.of(education1));
            when(educationRepository.saveAndFlush(education1)).thenReturn(education1);

            Education result = educationService.updateEducation(1L, updatedData);

            assertNotNull(result);
            assertEquals("MBA", result.getDiploma());
            assertEquals("HEC Paris", result.getUniversity());
            assertEquals("2025-06-30", result.getEndDate());
            assertEquals("MBA en finance", result.getDescription());
            verify(educationRepository).findById(1L);
            verify(educationRepository).saveAndFlush(education1);
        }

        @Test
        @DisplayName("Should throw NullPointerException when education not found")
        void shouldThrowWhenEducationNotFound() {
            Education updatedData = new Education();
            updatedData.setDiploma("MBA");

            when(educationRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(NullPointerException.class,
                    () -> educationService.updateEducation(99L, updatedData));
            verify(educationRepository).findById(99L);
            verify(educationRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("deleteEducation Tests")
    class DeleteEducationTests {

        @Test
        @DisplayName("Should delete education when found")
        void shouldDeleteEducationWhenFound() {
            when(educationRepository.existsById(1L)).thenReturn(true);

            educationService.deleteEducation(1L);

            verify(educationRepository).existsById(1L);
            verify(educationRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenEducationNotFound() {
            when(educationRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> educationService.deleteEducation(99L));
            verify(educationRepository).existsById(99L);
            verify(educationRepository, never()).deleteById(anyLong());
        }
    }
}
