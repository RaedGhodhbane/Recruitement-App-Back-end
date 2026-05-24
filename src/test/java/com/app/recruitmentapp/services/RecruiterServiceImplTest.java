package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.RecruiterDTO;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.mapper.EntityMapper;
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
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private RecruiterServiceImpl recruiterService;

    private Recruiter recruiter1;
    private Recruiter recruiter2;
    private RecruiterDTO recruiter1DTO;
    private RecruiterDTO recruiter2DTO;

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

        recruiter1DTO = new RecruiterDTO();
        recruiter1DTO.setId(1L);
        recruiter1DTO.setEmail("recruiter1@test.com");
        recruiter1DTO.setName("Dupont");
        recruiter1DTO.setFirstName("Jean");
        recruiter1DTO.setActive(true);
        recruiter1DTO.setRole("RECRUITER");
        recruiter1DTO.setCompanyName("TechCorp");
        recruiter1DTO.setPhone("123456789");
        recruiter1DTO.setAddress("Paris");
        recruiter1DTO.setWebsite("https://techcorp.com");
        recruiter1DTO.setDescription("Recruteur IT");
        recruiter1DTO.setCreationDate(Instant.parse("2026-01-01T00:00:00Z"));

        recruiter2DTO = new RecruiterDTO();
        recruiter2DTO.setId(2L);
        recruiter2DTO.setEmail("recruiter2@test.com");
        recruiter2DTO.setName("Martin");
        recruiter2DTO.setFirstName("Sophie");
        recruiter2DTO.setActive(false);
        recruiter2DTO.setRole("RECRUITER");
        recruiter2DTO.setCompanyName("StartupLab");
        recruiter2DTO.setPhone("987654321");
        recruiter2DTO.setAddress("Lyon");
        recruiter2DTO.setWebsite("https://startuplab.com");
        recruiter2DTO.setDescription("RH");
        recruiter2DTO.setCreationDate(Instant.parse("2026-02-01T00:00:00Z"));
    }

    @Nested
    @DisplayName("getAllRecruiters Tests")
    class GetAllRecruitersTests {

        @Test
        @DisplayName("Should return all recruiters")
        void shouldGetAllRecruitersSuccessfully() {
            List<Recruiter> mockRecruiters = List.of(recruiter1, recruiter2);
            when(recruiterRepository.findAll()).thenReturn(mockRecruiters);
            when(entityMapper.toRecruiterDTOList(mockRecruiters)).thenReturn(List.of(recruiter1DTO, recruiter2DTO));

            List<RecruiterDTO> result = recruiterService.getAllRecruiters();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("recruiter1@test.com", result.get(0).getEmail());
            assertEquals("recruiter2@test.com", result.get(1).getEmail());
            verify(recruiterRepository).findAll();
            verify(entityMapper).toRecruiterDTOList(mockRecruiters);
        }
    }

    @Nested
    @DisplayName("getRecruiterById Tests")
    class GetRecruiterByIdTests {

        @Test
        @DisplayName("Should return recruiter when found")
        void shouldReturnRecruiterWhenFound() {
            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter1));
            when(entityMapper.toRecruiterDTO(recruiter1)).thenReturn(recruiter1DTO);

            Optional<RecruiterDTO> result = recruiterService.getRecruiterById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("TechCorp", result.get().getCompanyName());
            verify(recruiterRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(recruiterRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<RecruiterDTO> result = recruiterService.getRecruiterById(99L);

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
            RecruiterDTO resultDTO = new RecruiterDTO();
            resultDTO.setId(3L);
            resultDTO.setEmail("new@test.com");
            resultDTO.setName("Durand");
            resultDTO.setFirstName("Alice");
            resultDTO.setPhone("555123456");
            resultDTO.setActive(false);
            resultDTO.setRole("RECRUITER");

            when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("rawPass")).thenReturn("encoded-rawPass");
            when(recruiterRepository.save(any(Recruiter.class))).thenAnswer(invocation -> {
                Recruiter saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toRecruiterDTO(any(Recruiter.class))).thenReturn(resultDTO);

            RecruiterDTO result = recruiterService.registerRecruiter(
                    "new@test.com", "rawPass", "Durand", "Alice", "555123456");

            assertNotNull(result);
            assertEquals("new@test.com", result.getEmail());
            assertEquals("Durand", result.getName());
            assertEquals("Alice", result.getFirstName());
            assertEquals("555123456", result.getPhone());
            assertFalse(result.isActive());
            assertEquals("RECRUITER", result.getRole());
            verify(userRepository).findByEmail("new@test.com");
            verify(passwordEncoder).encode("rawPass");
            verify(recruiterRepository).save(any(Recruiter.class));
            verify(entityMapper).toRecruiterDTO(any(Recruiter.class));
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
            RecruiterDTO inputDTO = new RecruiterDTO();
            inputDTO.setEmail("pic@test.com");
            inputDTO.setName("Picard");
            inputDTO.setFirstName("Thomas");
            inputDTO.setCompanyName("FotoCorp");

            Recruiter inputEntity = new Recruiter();
            inputEntity.setEmail("pic@test.com");
            inputEntity.setName("Picard");
            inputEntity.setFirstName("Thomas");
            inputEntity.setCompanyName("FotoCorp");

            RecruiterDTO resultDTO = new RecruiterDTO();
            resultDTO.setId(3L);
            resultDTO.setEmail("pic@test.com");
            resultDTO.setName("Picard");
            resultDTO.setFirstName("Thomas");
            resultDTO.setRole("RECRUITER");
            resultDTO.setActive(false);
            resultDTO.setCompanyName("FotoCorp");

            when(entityMapper.toRecruiterEntity(inputDTO)).thenReturn(inputEntity);
            when(recruiterRepository.save(any(Recruiter.class))).thenAnswer(invocation -> {
                Recruiter saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toRecruiterDTO(any(Recruiter.class))).thenReturn(resultDTO);

            RecruiterDTO result = recruiterService.addRecruiterWithPicture(inputDTO, null);

            assertNotNull(result);
            assertEquals("pic@test.com", result.getEmail());
            assertEquals("Picard", result.getName());
            assertEquals("RECRUITER", result.getRole());
            assertFalse(result.isActive());
            assertNull(result.getImage());
            verify(entityMapper).toRecruiterEntity(inputDTO);
            verify(recruiterRepository).save(any(Recruiter.class));
            verify(entityMapper).toRecruiterDTO(any(Recruiter.class));
        }
    }

    @Nested
    @DisplayName("updateRecruiter Tests")
    class UpdateRecruiterTests {

        @Test
        @DisplayName("Should update recruiter when found")
        void shouldUpdateRecruiterWhenFound() {
            RecruiterDTO updatedData = new RecruiterDTO();
            updatedData.setName("UpdatedName");
            updatedData.setFirstName("UpdatedFirst");
            updatedData.setEmail("updated@test.com");
            updatedData.setAddress("Marseille");
            updatedData.setCompanyName("NewCorp");
            updatedData.setPhone("111222333");
            updatedData.setWebsite("https://newcorp.com");
            updatedData.setDescription("Updated desc");
            updatedData.setCreationDate(Instant.parse("2026-03-01T00:00:00Z"));
            updatedData.setRole("RECRUITER");

            RecruiterDTO resultDTO = new RecruiterDTO();
            resultDTO.setId(1L);
            resultDTO.setName("UpdatedName");
            resultDTO.setFirstName("UpdatedFirst");
            resultDTO.setEmail("updated@test.com");
            resultDTO.setAddress("Marseille");
            resultDTO.setCompanyName("NewCorp");
            resultDTO.setPhone("111222333");
            resultDTO.setWebsite("https://newcorp.com");
            resultDTO.setDescription("Updated desc");
            resultDTO.setCreationDate(Instant.parse("2026-03-01T00:00:00Z"));
            resultDTO.setRole("RECRUITER");

            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter1));
            when(recruiterRepository.saveAndFlush(recruiter1)).thenReturn(recruiter1);
            when(entityMapper.toRecruiterDTO(recruiter1)).thenReturn(resultDTO);

            RecruiterDTO result = recruiterService.updateRecruiter(1L, updatedData);

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
            assertEquals("RECRUITER", result.getRole());
            verify(recruiterRepository).findById(1L);
            verify(recruiterRepository).saveAndFlush(recruiter1);
            verify(entityMapper).toRecruiterDTO(recruiter1);
        }

        @Test
        @DisplayName("Should update recruiter with partial fields")
        void shouldUpdateRecruiterWithPartialFields() {
            RecruiterDTO updatedData = new RecruiterDTO();
            updatedData.setCompanyName("UpdatedCompany");
            updatedData.setPhone("999888777");

            RecruiterDTO resultDTO = new RecruiterDTO();
            resultDTO.setId(1L);
            resultDTO.setCompanyName("UpdatedCompany");
            resultDTO.setPhone("999888777");
            resultDTO.setName("Dupont");
            resultDTO.setFirstName("Jean");
            resultDTO.setEmail("recruiter1@test.com");
            resultDTO.setAddress("Paris");

            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter1));
            when(recruiterRepository.saveAndFlush(recruiter1)).thenReturn(recruiter1);
            when(entityMapper.toRecruiterDTO(recruiter1)).thenReturn(resultDTO);

            RecruiterDTO result = recruiterService.updateRecruiter(1L, updatedData);

            assertNotNull(result);
            assertEquals("UpdatedCompany", result.getCompanyName());
            assertEquals("999888777", result.getPhone());
            assertEquals("Dupont", result.getName());
            assertEquals("Jean", result.getFirstName());
            assertEquals("recruiter1@test.com", result.getEmail());
            assertEquals("Paris", result.getAddress());
            verify(recruiterRepository).findById(1L);
            verify(recruiterRepository).saveAndFlush(recruiter1);
            verify(entityMapper).toRecruiterDTO(recruiter1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when recruiter not found")
        void shouldThrowWhenRecruiterNotFound() {
            RecruiterDTO updatedData = new RecruiterDTO();
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
