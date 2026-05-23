package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import com.app.recruitmentapp.repositories.UserRepository;
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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecruiterServiceImpl Unit Tests")
class RecruiterServiceImplTest {

    @Mock
    private RecruiterRepository recruiterRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RecruiterServiceImpl recruiterService;

    private Recruiter recruiter1;
    private Recruiter recruiter2;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(recruiterService, "uploadDir", "test-uploads");

        recruiter1 = new Recruiter();
        recruiter1.setId(1L);
        recruiter1.setEmail("recruiter1@test.com");
        recruiter1.setName("Dupont");
        recruiter1.setFirstName("Jean");
        recruiter1.setPassword("encoded-pass-1");
        recruiter1.setActive(true);
        recruiter1.setRole(Role.RECRUITER);
        recruiter1.setCompanyName("TechCorp");
        recruiter1.setPhone("123456789");
        recruiter1.setAddress("Paris");
        recruiter1.setWebsite("https://techcorp.com");
        recruiter1.setDescription("Recruteur IT");
        recruiter1.setCreationDate(Instant.parse("2026-01-01T00:00:00Z"));

        recruiter2 = new Recruiter();
        recruiter2.setId(2L);
        recruiter2.setEmail("recruiter2@test.com");
        recruiter2.setName("Martin");
        recruiter2.setFirstName("Sophie");
        recruiter2.setPassword("encoded-pass-2");
        recruiter2.setActive(false);
        recruiter2.setRole(Role.RECRUITER);
        recruiter2.setCompanyName("StartupLab");
        recruiter2.setPhone("987654321");
        recruiter2.setAddress("Lyon");
        recruiter2.setWebsite("https://startuplab.com");
        recruiter2.setDescription("RH");
        recruiter2.setCreationDate(Instant.parse("2026-02-01T00:00:00Z"));
    }

    @Nested
    @DisplayName("getAllRecruiters Tests")
    class GetAllRecruitersTests {

        @Test
        @DisplayName("Should return all recruiters")
        void shouldGetAllRecruitersSuccessfully() {
            List<Recruiter> mockRecruiters = List.of(recruiter1, recruiter2);
            when(recruiterRepository.findAll()).thenReturn(mockRecruiters);

            List<Recruiter> result = recruiterService.getAllRecruiters();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("recruiter1@test.com", result.get(0).getEmail());
            assertEquals("recruiter2@test.com", result.get(1).getEmail());
            verify(recruiterRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getRecruiterById Tests")
    class GetRecruiterByIdTests {

        @Test
        @DisplayName("Should return recruiter when found")
        void shouldReturnRecruiterWhenFound() {
            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter1));

            Optional<Recruiter> result = recruiterService.getRecruiterById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("TechCorp", result.get().getCompanyName());
            verify(recruiterRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(recruiterRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Recruiter> result = recruiterService.getRecruiterById(99L);

            assertFalse(result.isPresent());
            verify(recruiterRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("registerRecruiter Tests")
    class RegisterRecruiterTests {

        @Test
        @DisplayName("Should register recruiter successfully")
        void shouldRegisterRecruiterSuccessfully() {
            when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("rawPass")).thenReturn("encoded-rawPass");
            when(recruiterRepository.save(any(Recruiter.class))).thenAnswer(invocation -> {
                Recruiter saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Recruiter result = recruiterService.registerRecruiter(
                    "new@test.com", "rawPass", "Durand", "Alice", "555123456");

            assertNotNull(result);
            assertEquals("new@test.com", result.getEmail());
            assertEquals("Durand", result.getName());
            assertEquals("Alice", result.getFirstName());
            assertEquals("555123456", result.getPhone());
            assertEquals("encoded-rawPass", result.getPassword());
            assertFalse(result.getActive());
            assertEquals(Role.RECRUITER, result.getRole());
            verify(userRepository).findByEmail("new@test.com");
            verify(passwordEncoder).encode("rawPass");
            verify(recruiterRepository).save(any(Recruiter.class));
        }

        @Test
        @DisplayName("Should throw EmailAlreadyUsedException when email exists")
        void shouldThrowEmailAlreadyUsedException() {
            Recruiter existing = new Recruiter();
            existing.setEmail("existing@test.com");
            when(userRepository.findByEmail("existing@test.com")).thenReturn(Optional.of(existing));

            assertThrows(EmailAlreadyUsedException.class,
                    () -> recruiterService.registerRecruiter(
                            "existing@test.com", "pass", "Name", "First", "123"));
            verify(userRepository).findByEmail("existing@test.com");
            verify(passwordEncoder, never()).encode(any());
            verify(recruiterRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("addRecruiterWithPicture Tests")
    class AddRecruiterWithPictureTests {

        @Test
        @DisplayName("Should add recruiter without image when imageFile is null")
        void shouldAddRecruiterWithoutImage() {
            Recruiter newRecruiter = new Recruiter();
            newRecruiter.setEmail("pic@test.com");
            newRecruiter.setName("Picard");
            newRecruiter.setFirstName("Thomas");
            newRecruiter.setCompanyName("FotoCorp");

            when(recruiterRepository.save(any(Recruiter.class))).thenAnswer(invocation -> {
                Recruiter saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Recruiter result = recruiterService.addRecruiterWithPicture(newRecruiter, null);

            assertNotNull(result);
            assertEquals("pic@test.com", result.getEmail());
            assertEquals("Picard", result.getName());
            assertEquals(Role.RECRUITER, result.getRole());
            assertFalse(result.getActive());
            assertNull(result.getImage());
            verify(recruiterRepository).save(any(Recruiter.class));
        }
    }

    @Nested
    @DisplayName("updateRecruiter Tests")
    class UpdateRecruiterTests {

        @Test
        @DisplayName("Should update recruiter when found")
        void shouldUpdateRecruiterWhenFound() {
            Recruiter updatedData = new Recruiter();
            updatedData.setName("UpdatedName");
            updatedData.setFirstName("UpdatedFirst");
            updatedData.setEmail("updated@test.com");
            updatedData.setAddress("Marseille");
            updatedData.setCompanyName("NewCorp");
            updatedData.setPhone("111222333");
            updatedData.setWebsite("https://newcorp.com");
            updatedData.setDescription("Updated desc");
            updatedData.setCreationDate(Instant.parse("2026-03-01T00:00:00Z"));
            updatedData.setRole(Role.RECRUITER);

            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter1));
            when(recruiterRepository.saveAndFlush(recruiter1)).thenReturn(recruiter1);

            Recruiter result = recruiterService.updateRecruiter(1L, updatedData);

            assertNotNull(result);
            assertEquals("UpdatedName", result.getName());
            assertEquals("UpdatedFirst", result.getFirstName());
            assertEquals("updated@test.com", result.getEmail());
            assertEquals("Marseille", result.getAddress());
            assertEquals("NewCorp", result.getCompanyName());
            assertEquals("111222333", result.getPhone());
            assertEquals("https://newcorp.com", result.getWebsite());
            assertEquals("Updated desc", result.getDescription());
            assertEquals(Instant.parse("2026-03-01T00:00:00Z"), result.getCreationDate());
            assertEquals(Role.RECRUITER, result.getRole());
            verify(recruiterRepository).findById(1L);
            verify(recruiterRepository).saveAndFlush(recruiter1);
        }

        @Test
        @DisplayName("Should update recruiter with partial fields")
        void shouldUpdateRecruiterWithPartialFields() {
            Recruiter updatedData = new Recruiter();
            updatedData.setCompanyName("UpdatedCompany");
            updatedData.setPhone("999888777");

            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter1));
            when(recruiterRepository.saveAndFlush(recruiter1)).thenReturn(recruiter1);

            Recruiter result = recruiterService.updateRecruiter(1L, updatedData);

            assertNotNull(result);
            assertEquals("UpdatedCompany", result.getCompanyName());
            assertEquals("999888777", result.getPhone());
            assertEquals("Dupont", result.getName());
            assertEquals("Jean", result.getFirstName());
            assertEquals("recruiter1@test.com", result.getEmail());
            assertEquals("Paris", result.getAddress());
            verify(recruiterRepository).findById(1L);
            verify(recruiterRepository).saveAndFlush(recruiter1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when recruiter not found")
        void shouldThrowWhenRecruiterNotFound() {
            Recruiter updatedData = new Recruiter();
            updatedData.setName("Any");

            when(recruiterRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> recruiterService.updateRecruiter(99L, updatedData));
            verify(recruiterRepository).findById(99L);
            verify(recruiterRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("deleteRecruiter Tests")
    class DeleteRecruiterTests {

        @Test
        @DisplayName("Should delete recruiter when found")
        void shouldDeleteRecruiterWhenFound() {
            when(recruiterRepository.existsById(1L)).thenReturn(true);

            recruiterService.deleteRecruiter(1L);

            verify(recruiterRepository).existsById(1L);
            verify(recruiterRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenRecruiterNotFound() {
            when(recruiterRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> recruiterService.deleteRecruiter(99L));
            verify(recruiterRepository).existsById(99L);
            verify(recruiterRepository, never()).deleteById(anyLong());
        }
    }
}
