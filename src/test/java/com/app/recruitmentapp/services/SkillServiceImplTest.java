package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.SkillDTO;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Skill;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.SkillRepository;
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
@DisplayName("SkillServiceImpl Unit Tests")
class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;
    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private SkillServiceImpl skillService;

    private Candidate candidate;
    private Skill skill1;
    private Skill skill2;
    private SkillDTO skill1DTO;
    private SkillDTO skill2DTO;

    @BeforeEach
    void setUp() {
        candidate = new Candidate();
        candidate.setId(1L);
        candidate.setEmail("candidate@test.com");

        skill1 = new Skill();
        skill1.setId(1L);
        skill1.setTitle("Java");
        skill1.setPercentage("90");
        skill1.setCandidate(candidate);

        skill2 = new Skill();
        skill2.setId(2L);
        skill2.setTitle("Python");
        skill2.setPercentage("75");
        skill2.setCandidate(candidate);

        skill1DTO = new SkillDTO();
        skill1DTO.setId(1L);
        skill1DTO.setTitle("Java");
        skill1DTO.setPercentage("90");
        skill1DTO.setCandidateId(1L);

        skill2DTO = new SkillDTO();
        skill2DTO.setId(2L);
        skill2DTO.setTitle("Python");
        skill2DTO.setPercentage("75");
        skill2DTO.setCandidateId(1L);
    }

    @Nested
    @DisplayName("getAllSkills Tests")
    class GetAllSkillsTests {

        @Test
        @DisplayName("Should return all skills")
        void shouldGetAllSkillsSuccessfully() {
            List<Skill> mockSkills = List.of(skill1, skill2);
            when(skillRepository.findAll()).thenReturn(mockSkills);
            when(entityMapper.toSkillDTOList(mockSkills)).thenReturn(List.of(skill1DTO, skill2DTO));

            List<SkillDTO> result = skillService.getAllSkills();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Java", result.get(0).getTitle());
            assertEquals("Python", result.get(1).getTitle());
            verify(skillRepository).findAll();
            verify(entityMapper).toSkillDTOList(mockSkills);
        }
    }

    @Nested
    @DisplayName("getSkillById Tests")
    class GetSkillByIdTests {

        @Test
        @DisplayName("Should return skill when found")
        void shouldReturnSkillWhenFound() {
            when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
            when(entityMapper.toSkillDTO(skill1)).thenReturn(skill1DTO);

            Optional<SkillDTO> result = skillService.getSkillById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("90", result.get().getPercentage());
            verify(skillRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(skillRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<SkillDTO> result = skillService.getSkillById(99L);

            assertFalse(result.isPresent());
            verify(skillRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("saveSkill Tests")
    class SaveSkillTests {

        @Test
        @DisplayName("Should save skill successfully")
        void shouldSaveSkillSuccessfully() {
            SkillDTO newSkillDTO = new SkillDTO();
            newSkillDTO.setTitle("Spring Boot");
            newSkillDTO.setPercentage("85");

            Skill newSkillEntity = new Skill();
            newSkillEntity.setTitle("Spring Boot");
            newSkillEntity.setPercentage("85");

            SkillDTO resultDTO = new SkillDTO();
            resultDTO.setId(3L);
            resultDTO.setTitle("Spring Boot");
            resultDTO.setPercentage("85");
            resultDTO.setCandidateId(1L);

            when(entityMapper.toSkillEntity(newSkillDTO)).thenReturn(newSkillEntity);
            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
            when(skillRepository.save(any(Skill.class))).thenAnswer(invocation -> {
                Skill saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toSkillDTO(any(Skill.class))).thenReturn(resultDTO);

            SkillDTO result = skillService.saveSkill(newSkillDTO, 1L);

            assertNotNull(result);
            assertEquals("Spring Boot", result.getTitle());
            assertEquals("85", result.getPercentage());
            assertEquals(1L, result.getCandidateId());
            verify(entityMapper).toSkillEntity(newSkillDTO);
            verify(candidateRepository).findById(1L);
            verify(skillRepository).save(any(Skill.class));
            verify(entityMapper).toSkillDTO(any(Skill.class));
        }

        @Test
        @DisplayName("Should save skill with null candidate when candidate not found")
        void shouldSaveSkillWithNullCandidateWhenNotFound() {
            SkillDTO newSkillDTO = new SkillDTO();
            newSkillDTO.setTitle("Spring Boot");
            newSkillDTO.setPercentage("85");

            Skill newSkillEntity = new Skill();
            newSkillEntity.setTitle("Spring Boot");
            newSkillEntity.setPercentage("85");

            SkillDTO resultDTO = new SkillDTO();
            resultDTO.setId(3L);
            resultDTO.setTitle("Spring Boot");
            resultDTO.setPercentage("85");

            when(entityMapper.toSkillEntity(newSkillDTO)).thenReturn(newSkillEntity);
            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());
            when(skillRepository.save(any(Skill.class))).thenAnswer(invocation -> {
                Skill saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toSkillDTO(any(Skill.class))).thenReturn(resultDTO);

            SkillDTO result = skillService.saveSkill(newSkillDTO, 99L);

            assertNotNull(result);
            assertNull(result.getCandidateId());
            verify(entityMapper).toSkillEntity(newSkillDTO);
            verify(candidateRepository).findById(99L);
            verify(skillRepository).save(any(Skill.class));
            verify(entityMapper).toSkillDTO(any(Skill.class));
        }
    }

    @Nested
    @DisplayName("updateSkill Tests")
    class UpdateSkillTests {

        @Test
        @DisplayName("Should update skill when found")
        void shouldUpdateSkillWhenFound() {
            SkillDTO updatedData = new SkillDTO();
            updatedData.setTitle("Kubernetes");
            updatedData.setPercentage("95");

            SkillDTO resultDTO = new SkillDTO();
            resultDTO.setId(1L);
            resultDTO.setTitle("Kubernetes");
            resultDTO.setPercentage("95");
            resultDTO.setCandidateId(1L);

            when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
            when(skillRepository.saveAndFlush(skill1)).thenReturn(skill1);
            when(entityMapper.toSkillDTO(skill1)).thenReturn(resultDTO);

            SkillDTO result = skillService.updateSkill(1L, updatedData);

            assertNotNull(result);
            assertEquals("Kubernetes", result.getTitle());
            assertEquals("95", result.getPercentage());
            assertEquals(1L, result.getCandidateId());
            verify(skillRepository).findById(1L);
            verify(skillRepository).saveAndFlush(skill1);
            verify(entityMapper).toSkillDTO(skill1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when skill not found")
        void shouldThrowWhenSkillNotFound() {
            SkillDTO updatedData = new SkillDTO();
            updatedData.setTitle("Kubernetes");

            when(skillRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> skillService.updateSkill(99L, updatedData));
            verify(skillRepository).findById(99L);
            verify(skillRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("deleteSkill Tests")
    class DeleteSkillTests {

        @Test
        @DisplayName("Should delete skill when found")
        void shouldDeleteSkillWhenFound() {
            when(skillRepository.existsById(1L)).thenReturn(true);

            skillService.deleteSkill(1L);

            verify(skillRepository).existsById(1L);
            verify(skillRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenSkillNotFound() {
            when(skillRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> skillService.deleteSkill(99L));
            verify(skillRepository).existsById(99L);
            verify(skillRepository, never()).deleteById(anyLong());
        }
    }
}
