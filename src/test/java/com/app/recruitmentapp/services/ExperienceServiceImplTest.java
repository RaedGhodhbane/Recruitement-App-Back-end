package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.ExperienceDTO;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Experience;
import com.app.recruitmentapp.mapper.EntityMapper;
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
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private ExperienceServiceImpl experienceService;

    private Experience experience1;
    private Experience experience2;
    private Candidate candidate;
    private ExperienceDTO experience1DTO;
    private ExperienceDTO experience2DTO;

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

        experience1DTO = new ExperienceDTO();
        experience1DTO.setId(1L);
        experience1DTO.setCompanyName("Google");
        experience1DTO.setJobTitle("Software Engineer");
        experience1DTO.setStartExpDate(Date.valueOf("2022-01-01"));
        experience1DTO.setEndExpDate(Date.valueOf("2024-06-30"));
        experience1DTO.setDescription("Développement backend");
        experience1DTO.setCandidateId(1L);

        experience2DTO = new ExperienceDTO();
        experience2DTO.setId(2L);
        experience2DTO.setCompanyName("Microsoft");
        experience2DTO.setJobTitle("Intern");
        experience2DTO.setStartExpDate(Date.valueOf("2021-06-01"));
        experience2DTO.setEndExpDate(Date.valueOf("2021-12-31"));
        experience2DTO.setDescription("Stage été");
        experience2DTO.setCandidateId(1L);
    }

    @Nested
    @DisplayName("getAllExperiences Tests")
    class GetAllExperiencesTests {

        @Test
        @DisplayName("Should return all experiences")
        void shouldGetAllExperiencesSuccessfully() {
            List<Experience> mockExperiences = List.of(experience1, experience2);
            when(experienceRepository.findAll()).thenReturn(mockExperiences);
            when(entityMapper.toExperienceDTOList(mockExperiences)).thenReturn(List.of(experience1DTO, experience2DTO));

            List<ExperienceDTO> result = experienceService.getAllExperiences();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Google", result.get(0).getCompanyName());
            assertEquals("Microsoft", result.get(1).getCompanyName());
            verify(experienceRepository).findAll();
            verify(entityMapper).toExperienceDTOList(mockExperiences);
        }
    }

    @Nested
    @DisplayName("getExperienceById Tests")
    class GetExperienceByIdTests {

        @Test
        @DisplayName("Should return experience when found")
        void shouldReturnExperienceWhenFound() {
            when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience1));
            when(entityMapper.toExperienceDTO(experience1)).thenReturn(experience1DTO);

            Optional<ExperienceDTO> result = experienceService.getExperienceById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("Software Engineer", result.get().getJobTitle());
            verify(experienceRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(experienceRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<ExperienceDTO> result = experienceService.getExperienceById(99L);

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
            ExperienceDTO newExperienceDTO = new ExperienceDTO();
            newExperienceDTO.setCompanyName("Amazon");
            newExperienceDTO.setJobTitle("SDE");
            newExperienceDTO.setStartExpDate(Date.valueOf("2023-01-01"));
            newExperienceDTO.setEndExpDate(Date.valueOf("2025-06-30"));
            newExperienceDTO.setDescription("Cloud computing");

            Experience newExperienceEntity = new Experience();
            newExperienceEntity.setCompanyName("Amazon");
            newExperienceEntity.setJobTitle("SDE");
            newExperienceEntity.setStartExpDate(Date.valueOf("2023-01-01"));
            newExperienceEntity.setEndExpDate(Date.valueOf("2025-06-30"));
            newExperienceEntity.setDescription("Cloud computing");

            ExperienceDTO resultDTO = new ExperienceDTO();
            resultDTO.setId(3L);
            resultDTO.setCompanyName("Amazon");
            resultDTO.setJobTitle("SDE");
            resultDTO.setStartExpDate(Date.valueOf("2023-01-01"));
            resultDTO.setEndExpDate(Date.valueOf("2025-06-30"));
            resultDTO.setDescription("Cloud computing");
            resultDTO.setCandidateId(1L);

            when(entityMapper.toExperienceEntity(newExperienceDTO)).thenReturn(newExperienceEntity);
            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
            when(experienceRepository.save(any(Experience.class))).thenAnswer(invocation -> {
                Experience saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toExperienceDTO(any(Experience.class))).thenReturn(resultDTO);

            ExperienceDTO result = experienceService.saveExperience(newExperienceDTO, 1L);

            assertNotNull(result);
            assertEquals("Amazon", result.getCompanyName());
            assertEquals("SDE", result.getJobTitle());
            assertEquals(1L, result.getCandidateId());
            verify(entityMapper).toExperienceEntity(newExperienceDTO);
            verify(candidateRepository).findById(1L);
            verify(experienceRepository).save(any(Experience.class));
            verify(entityMapper).toExperienceDTO(any(Experience.class));
        }

        @Test
        @DisplayName("Should save experience with null candidate when candidate not found")
        void shouldSaveExperienceWithNullCandidateWhenNotFound() {
            ExperienceDTO newExperienceDTO = new ExperienceDTO();
            newExperienceDTO.setCompanyName("Amazon");
            newExperienceDTO.setJobTitle("SDE");

            Experience newExperienceEntity = new Experience();
            newExperienceEntity.setCompanyName("Amazon");
            newExperienceEntity.setJobTitle("SDE");

            ExperienceDTO resultDTO = new ExperienceDTO();
            resultDTO.setId(3L);
            resultDTO.setCompanyName("Amazon");
            resultDTO.setJobTitle("SDE");

            when(entityMapper.toExperienceEntity(newExperienceDTO)).thenReturn(newExperienceEntity);
            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());
            when(experienceRepository.save(any(Experience.class))).thenAnswer(invocation -> {
                Experience saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toExperienceDTO(any(Experience.class))).thenReturn(resultDTO);

            ExperienceDTO result = experienceService.saveExperience(newExperienceDTO, 99L);

            assertNotNull(result);
            assertNull(result.getCandidateId());
            verify(entityMapper).toExperienceEntity(newExperienceDTO);
            verify(candidateRepository).findById(99L);
            verify(experienceRepository).save(any(Experience.class));
            verify(entityMapper).toExperienceDTO(any(Experience.class));
        }
    }

    @Nested
    @DisplayName("updateExperience Tests")
    class UpdateExperienceTests {

        @Test
        @DisplayName("Should update experience when found")
        void shouldUpdateExperienceWhenFound() {
            ExperienceDTO updatedData = new ExperienceDTO();
            updatedData.setCompanyName("Apple");
            updatedData.setJobTitle("iOS Developer");
            updatedData.setStartExpDate(Date.valueOf("2023-03-01"));
            updatedData.setEndExpDate(Date.valueOf("2025-09-30"));
            updatedData.setDescription("Développement iOS");

            ExperienceDTO resultDTO = new ExperienceDTO();
            resultDTO.setId(1L);
            resultDTO.setCompanyName("Apple");
            resultDTO.setJobTitle("iOS Developer");
            resultDTO.setStartExpDate(Date.valueOf("2023-03-01"));
            resultDTO.setEndExpDate(Date.valueOf("2025-09-30"));
            resultDTO.setDescription("Développement iOS");
            resultDTO.setCandidateId(1L);

            when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience1));
            when(experienceRepository.saveAndFlush(experience1)).thenReturn(experience1);
            when(entityMapper.toExperienceDTO(experience1)).thenReturn(resultDTO);

            ExperienceDTO result = experienceService.updateExperience(1L, updatedData);

            assertNotNull(result);
            assertEquals("Apple", result.getCompanyName());
            assertEquals("iOS Developer", result.getJobTitle());
            assertEquals(Date.valueOf("2023-03-01"), result.getStartExpDate());
            assertEquals(Date.valueOf("2025-09-30"), result.getEndExpDate());
            assertEquals("Développement iOS", result.getDescription());
            verify(experienceRepository).findById(1L);
            verify(experienceRepository).saveAndFlush(experience1);
            verify(entityMapper).toExperienceDTO(experience1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when experience not found")
        void shouldThrowWhenExperienceNotFound() {
            ExperienceDTO updatedData = new ExperienceDTO();
            updatedData.setCompanyName("Apple");

            when(experienceRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
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
