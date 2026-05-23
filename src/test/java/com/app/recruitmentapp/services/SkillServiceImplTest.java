package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Skill;
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
    @InjectMocks
    private SkillServiceImpl skillService;

    private Candidate candidate;
    private Skill skill1;
    private Skill skill2;

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
    }

    @Nested
    @DisplayName("getAllSkills Tests")
    class GetAllSkillsTests {

        @Test
        @DisplayName("Should return all skills")
        void shouldGetAllSkillsSuccessfully() {
            List<Skill> mockSkills = List.of(skill1, skill2);
            when(skillRepository.findAll()).thenReturn(mockSkills);

            List<Skill> result = skillService.getAllSkills();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Java", result.get(0).getTitle());
            assertEquals("Python", result.get(1).getTitle());
            verify(skillRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getSkillById Tests")
    class GetSkillByIdTests {

        @Test
        @DisplayName("Should return skill when found")
        void shouldReturnSkillWhenFound() {
            when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));

            Optional<Skill> result = skillService.getSkillById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("90", result.get().getPercentage());
            verify(skillRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(skillRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Skill> result = skillService.getSkillById(99L);

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
            Skill newSkill = new Skill();
            newSkill.setTitle("Spring Boot");
            newSkill.setPercentage("85");

            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
            when(skillRepository.save(any(Skill.class))).thenAnswer(invocation -> {
                Skill saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Skill result = skillService.saveSkill(newSkill, 1L);

            assertNotNull(result);
            assertEquals("Spring Boot", result.getTitle());
            assertEquals("85", result.getPercentage());
            assertEquals(candidate, result.getCandidate());
            verify(candidateRepository).findById(1L);
            verify(skillRepository).save(any(Skill.class));
        }

        @Test
        @DisplayName("Should save skill with null candidate when candidate not found")
        void shouldSaveSkillWithNullCandidateWhenNotFound() {
            Skill newSkill = new Skill();
            newSkill.setTitle("Spring Boot");
            newSkill.setPercentage("85");

            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());
            when(skillRepository.save(any(Skill.class))).thenAnswer(invocation -> {
                Skill saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Skill result = skillService.saveSkill(newSkill, 99L);

            assertNotNull(result);
            assertNull(result.getCandidate());
            verify(candidateRepository).findById(99L);
            verify(skillRepository).save(any(Skill.class));
        }
    }

    @Nested
    @DisplayName("updateSkill Tests")
    class UpdateSkillTests {

        @Test
        @DisplayName("Should update skill when found")
        void shouldUpdateSkillWhenFound() {
            Skill updatedData = new Skill();
            updatedData.setTitle("Kubernetes");
            updatedData.setPercentage("95");
            updatedData.setCandidate(candidate);

            when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
            when(skillRepository.saveAndFlush(skill1)).thenReturn(skill1);

            Skill result = skillService.updateSkill(1L, updatedData);

            assertNotNull(result);
            assertEquals("Kubernetes", result.getTitle());
            assertEquals("95", result.getPercentage());
            assertEquals(candidate, result.getCandidate());
            verify(skillRepository).findById(1L);
            verify(skillRepository).saveAndFlush(skill1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when skill not found")
        void shouldThrowWhenSkillNotFound() {
            Skill updatedData = new Skill();
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
