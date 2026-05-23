package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.ChangePassword;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import com.app.recruitmentapp.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CandidateServiceImpl Unit Tests")
class CandidateServiceImplTest {

    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private CandidateServiceImpl candidateService;

    private Candidate candidate1;
    private Candidate candidate2;
    private ChangePassword changePasswordRequest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(candidateService, "uploadDir", "test-uploads");

        candidate1 = new Candidate();
        candidate1.setId(1L);
        candidate1.setEmail("john@test.com");
        candidate1.setName("Doe");
        candidate1.setFirstName("John");
        candidate1.setPhone("123456789");
        candidate1.setPassword("encoded-pass-1");
        candidate1.setActive(true);
        candidate1.setRole(Role.CANDIDATE);
        candidate1.setDescription("Developer");

        candidate2 = new Candidate();
        candidate2.setId(2L);
        candidate2.setEmail("jane@test.com");
        candidate2.setName("Smith");
        candidate2.setFirstName("Jane");
        candidate2.setPhone("987654321");
        candidate2.setPassword("encoded-pass-2");
        candidate2.setActive(false);
        candidate2.setRole(Role.CANDIDATE);
        candidate2.setDescription("Designer");

        changePasswordRequest = new ChangePassword();
        changePasswordRequest.setCurrentPassword("oldPass");
        changePasswordRequest.setNewPassword("newPass");
        changePasswordRequest.setConfirmPassword("newPass");
    }

    @Nested
    @DisplayName("getAllCandidates Tests")
    class GetAllCandidatesTests {

        @Test
        @DisplayName("Should return all candidates")
        void shouldGetAllCandidatesSuccessfully() {
            List<Candidate> mockCandidates = List.of(candidate1, candidate2);
            when(candidateRepository.findAll()).thenReturn(mockCandidates);

            List<Candidate> result = candidateService.getAllCandidates();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("john@test.com", result.get(0).getEmail());
            assertEquals("jane@test.com", result.get(1).getEmail());
            verify(candidateRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getCandidateById Tests")
    class GetCandidateByIdTests {

        @Test
        @DisplayName("Should return candidate when found")
        void shouldReturnCandidateWhenFound() {
            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate1));

            Optional<Candidate> result = candidateService.getCandidateById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("john@test.com", result.get().getEmail());
            verify(candidateRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Candidate> result = candidateService.getCandidateById(99L);

            assertFalse(result.isPresent());
            verify(candidateRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("registerCandidate Tests")
    class RegisterCandidateTests {

        @Test
        @DisplayName("Should register candidate successfully")
        void shouldRegisterCandidateSuccessfully() {
            when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("rawPass")).thenReturn("encoded-rawPass");
            when(candidateRepository.save(any(Candidate.class))).thenAnswer(invocation -> {
                Candidate saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Candidate result = candidateService.registerCandidate(
                    "new@test.com", "rawPass", "Brown", "Charlie", "555123456");

            assertNotNull(result);
            assertEquals("new@test.com", result.getEmail());
            assertEquals("Brown", result.getName());
            assertEquals("Charlie", result.getFirstName());
            assertEquals("555123456", result.getPhone());
            assertEquals("encoded-rawPass", result.getPassword());
            assertFalse(result.getActive());
            assertEquals(Role.CANDIDATE, result.getRole());
            verify(userRepository).findByEmail("new@test.com");
            verify(passwordEncoder).encode("rawPass");
            verify(candidateRepository).save(any(Candidate.class));
        }

        @Test
        @DisplayName("Should throw EmailAlreadyUsedException when email exists")
        void shouldThrowEmailAlreadyUsedException() {
            Candidate existing = new Candidate();
            existing.setEmail("existing@test.com");
            when(userRepository.findByEmail("existing@test.com")).thenReturn(Optional.of(existing));

            assertThrows(EmailAlreadyUsedException.class,
                    () -> candidateService.registerCandidate(
                            "existing@test.com", "pass", "Name", "First", "123"));
            verify(userRepository).findByEmail("existing@test.com");
            verify(passwordEncoder, never()).encode(any());
            verify(candidateRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("updateCandidate Tests")
    class UpdateCandidateTests {

        @Test
        @DisplayName("Should update candidate when found")
        void shouldUpdateCandidateWhenFound() {
            Candidate updatedData = new Candidate();
            updatedData.setName("UpdatedName");
            updatedData.setFirstName("UpdatedFirst");
            updatedData.setEmail("updated@test.com");
            updatedData.setDescription("Updated desc");
            updatedData.setAddress("New address");
            updatedData.setTitle("Senior");
            updatedData.setPhone("111222333");
            updatedData.setActive(true);

            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate1));
            when(candidateRepository.saveAndFlush(candidate1)).thenReturn(candidate1);

            Candidate result = candidateService.updateCandidate(1L, updatedData);

            assertNotNull(result);
            assertEquals("UpdatedName", result.getName());
            assertEquals("UpdatedFirst", result.getFirstName());
            assertEquals("updated@test.com", result.getEmail());
            assertEquals("Updated desc", result.getDescription());
            assertEquals("New address", result.getAddress());
            assertEquals("Senior", result.getTitle());
            assertEquals("111222333", result.getPhone());
            assertTrue(result.getActive());
            verify(candidateRepository).findById(1L);
            verify(candidateRepository).saveAndFlush(candidate1);
        }

        @Test
        @DisplayName("Should throw NullPointerException when candidate not found")
        void shouldThrowWhenCandidateNotFound() {
            Candidate updatedData = new Candidate();
            updatedData.setName("Any");

            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(NullPointerException.class,
                    () -> candidateService.updateCandidate(99L, updatedData));
            verify(candidateRepository).findById(99L);
            verify(candidateRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("deleteCandidate Tests")
    class DeleteCandidateTests {

        @Test
        @DisplayName("Should delete candidate when found")
        void shouldDeleteCandidateWhenFound() {
            when(candidateRepository.existsById(1L)).thenReturn(true);

            candidateService.deleteCandidate(1L);

            verify(candidateRepository).existsById(1L);
            verify(candidateRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenCandidateNotFound() {
            when(candidateRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> candidateService.deleteCandidate(99L));
            verify(candidateRepository).existsById(99L);
            verify(candidateRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("changePassword Tests")
    class ChangePasswordTests {

        @Test
        @DisplayName("Should change password successfully")
        void shouldChangePasswordSuccessfully() {
            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate1));
            when(passwordEncoder.matches("oldPass", "encoded-pass-1")).thenReturn(true);
            when(passwordEncoder.encode("newPass")).thenReturn("encoded-newPass");
            when(candidateRepository.save(candidate1)).thenReturn(candidate1);

            String result = candidateService.changePassword(1L, changePasswordRequest);

            assertEquals("Mot de passe changé avec succès !", result);
            assertEquals("encoded-newPass", candidate1.getPassword());
            verify(candidateRepository).findById(1L);
            verify(passwordEncoder).matches("oldPass", "encoded-pass-1");
            verify(passwordEncoder).encode("newPass");
            verify(candidateRepository).save(candidate1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when candidate not found")
        void shouldThrowWhenCandidateNotFound() {
            when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> candidateService.changePassword(99L, changePasswordRequest));
            verify(candidateRepository).findById(99L);
            verify(passwordEncoder, never()).matches(any(), any());
            verify(candidateRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when current password is incorrect")
        void shouldThrowWhenCurrentPasswordIncorrect() {
            when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate1));
            when(passwordEncoder.matches("oldPass", "encoded-pass-1")).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> candidateService.changePassword(1L, changePasswordRequest));
            verify(candidateRepository).findById(1L);
            verify(passwordEncoder).matches("oldPass", "encoded-pass-1");
            verify(passwordEncoder, never()).encode(any());
            verify(candidateRepository, never()).save(any());
        }
    }
}
