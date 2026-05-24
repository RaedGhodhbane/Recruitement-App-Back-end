package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.EducationDTO;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Education;
import com.app.recruitmentapp.mapper.EntityMapper;
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
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private EducationServiceImpl educationService;

    private Education education1;
    private Education education2;
    private Candidate candidate;
    private EducationDTO education1DTO;
    private EducationDTO education2DTO;

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

        education1DTO = new EducationDTO();
        education1DTO.setId(1L);
        education1DTO.setDiploma("Master");
        education1DTO.setUniversity("Sorbonne");
        education1DTO.setEndDate("2024-06-30");
        education1DTO.setDescription("Master en informatique");
        education1DTO.setCandidateId(1L);

        education2DTO = new EducationDTO();
        education2DTO.setId(2L);
        education2DTO.setDiploma("Bachelor");
        education2DTO.setUniversity("Tunis");
        education2DTO.setEndDate("2022-06-30");
        education2DTO.setDescription("Licence en informatique");
        education2DTO.setCandidateId(1L);
    }

    @Nested
    @DisplayName("getAllEducations Tests")
    class GetAllEducationsTests {

        @Test
        @DisplayName("Should return all educations")
        void shouldGetAllEducationsSuccessfully() {
            List<Education> mockEducations = List.of(education1, education2);
            when(educationRepository.findAll()).thenReturn(mockEducations);
            when(entityMapper.toEducationDTOList(mockEducations)).thenReturn(List.of(education1DTO, education2DTO));

            List<EducationDTO> result = educationService.getAllEducations();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Master", result.get(0).getDiploma());
            assertEquals("Bachelor", result.get(1).getDiploma());
            verify(educationRepository).findAll();
            verify(entityMapper).toEducationDTOList(mockEducations);
        }
    }

    @Nested
    @DisplayName("getEducationById Tests")
    class GetEducationByIdTests {

        @Test
        @DisplayName("Should return education when found")
        void shouldReturnEducationWhenFound() {
            when(educationRepository.findById(1L)).thenReturn(Optional.of(education1));
            when(entityMapper.toEducationDTO(education1)).thenReturn(education1DTO);

            Optional<EducationDTO> result = educationService.getEducationById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("Sorbonne", result.get().getUniversity());
            verify(educationRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(educationRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<EducationDTO> result = educationService.getEducationById(99L);

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
            EducationDTO newEducationDTO = new EducationDTO();
            newEducationDTO.setDiploma("PhD");
            newEducationDTO.setUniversity("MIT");
            newEducationDTO.setEndDate("2027-06-30");
            newEducationDTO.setDescription("Doctorat en IA");

            Education newEducationEntity = new Education();
            newEducationEntity.setDiploma("PhD");
            newEducationEntity.setUniversity("MIT");
            newEducationEntity.setEndDate("2027-06-30");
            newEducationEntity.setDescription("Doctorat en IA");

            EducationDTO resultDTO = new EducationDTO();
            resultDTO.setId(3L);
            resultDTO.setDiploma("PhD");
            resultDTO.setUniversity("MIT");
            resultDTO.setEndDate("2027-06-30");
            resultDTO.setDescription("Doctorat en IA");
            resultDTO.setCandidateId(1L);

            when(entityMapper.toEducationEntity(newEducationDTO)).thenReturn(newEducationEntity);
            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
            when(educationRepository.save(any(Education.class))).thenAnswer(invocation -> {
                Education saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toEducationDTO(any(Education.class))).thenReturn(resultDTO);

            EducationDTO result = educationService.saveEducation(newEducationDTO, 1L);

            assertNotNull(result);
            assertEquals("PhD", result.getDiploma());
            assertEquals("MIT", result.getUniversity());
            assertEquals(1L, result.getCandidateId());
            verify(entityMapper).toEducationEntity(newEducationDTO);
            verify(candidateRepository).findById(1L);
            verify(educationRepository).save(any(Education.class));
            verify(entityMapper).toEducationDTO(any(Education.class));
        }

        @Test
        @DisplayName("Should save education with null candidate when candidate not found")
        void shouldSaveEducationWithNullCandidateWhenNotFound() {
            EducationDTO newEducationDTO = new EducationDTO();
            newEducationDTO.setDiploma("PhD");
            newEducationDTO.setUniversity("MIT");

            Education newEducationEntity = new Education();
            newEducationEntity.setDiploma("PhD");
            newEducationEntity.setUniversity("MIT");

            EducationDTO resultDTO = new EducationDTO();
            resultDTO.setId(3L);
            resultDTO.setDiploma("PhD");
            resultDTO.setUniversity("MIT");

            when(entityMapper.toEducationEntity(newEducationDTO)).thenReturn(newEducationEntity);
            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());
            when(educationRepository.save(any(Education.class))).thenAnswer(invocation -> {
                Education saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toEducationDTO(any(Education.class))).thenReturn(resultDTO);

            EducationDTO result = educationService.saveEducation(newEducationDTO, 99L);

            assertNotNull(result);
            assertNull(result.getCandidateId());
            verify(entityMapper).toEducationEntity(newEducationDTO);
            verify(candidateRepository).findById(99L);
            verify(educationRepository).save(any(Education.class));
            verify(entityMapper).toEducationDTO(any(Education.class));
        }
    }

    @Nested
    @DisplayName("updateEducation Tests")
    class UpdateEducationTests {

        @Test
        @DisplayName("Should update education when found")
        void shouldUpdateEducationWhenFound() {
            EducationDTO updatedData = new EducationDTO();
            updatedData.setDiploma("MBA");
            updatedData.setUniversity("HEC Paris");
            updatedData.setEndDate("2025-06-30");
            updatedData.setDescription("MBA en finance");

            EducationDTO resultDTO = new EducationDTO();
            resultDTO.setId(1L);
            resultDTO.setDiploma("MBA");
            resultDTO.setUniversity("HEC Paris");
            resultDTO.setEndDate("2025-06-30");
            resultDTO.setDescription("MBA en finance");
            resultDTO.setCandidateId(1L);

            when(educationRepository.findById(1L)).thenReturn(Optional.of(education1));
            when(educationRepository.saveAndFlush(education1)).thenReturn(education1);
            when(entityMapper.toEducationDTO(education1)).thenReturn(resultDTO);

            EducationDTO result = educationService.updateEducation(1L, updatedData);

            assertNotNull(result);
            assertEquals("MBA", result.getDiploma());
            assertEquals("HEC Paris", result.getUniversity());
            assertEquals("2025-06-30", result.getEndDate());
            assertEquals("MBA en finance", result.getDescription());
            verify(educationRepository).findById(1L);
            verify(educationRepository).saveAndFlush(education1);
            verify(entityMapper).toEducationDTO(education1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when education not found")
        void shouldThrowWhenEducationNotFound() {
            EducationDTO updatedData = new EducationDTO();
            updatedData.setDiploma("MBA");

            when(educationRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
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
