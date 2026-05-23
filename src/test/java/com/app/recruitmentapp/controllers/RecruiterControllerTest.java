package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.services.RecruiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecruiterController Unit Tests")
class RecruiterControllerTest {

    @Mock
    private RecruiterService recruiterService;

    @InjectMocks
    private RecruiterController recruiterController;

    private Recruiter testRecruiter;

    @BeforeEach
    void setUp() {
        testRecruiter = new Recruiter();
        testRecruiter.setId(1L);
        testRecruiter.setEmail("recruiter@test.com");
        testRecruiter.setName("Doe");
        testRecruiter.setFirstName("Jane");
        testRecruiter.setPhone("0123456789");
    }

    @Nested
    @DisplayName("GET /recruiter/recruiters")
    class GetAllRecruitersTests {

        @Test
        @DisplayName("Should return all recruiters")
        void shouldReturnAllRecruiters() {
            when(recruiterService.getAllRecruiters()).thenReturn(List.of(testRecruiter));

            List<Recruiter> result = recruiterController.getAllRecruiters();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /recruiter/{id}")
    class GetRecruiterByIdTests {

        @Test
        @DisplayName("Should return recruiter when found")
        void shouldReturnRecruiterWhenFound() {
            when(recruiterService.getRecruiterById(1L)).thenReturn(Optional.of(testRecruiter));

            ResponseEntity<Recruiter> response = recruiterController.getRecruiterById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(recruiterService.getRecruiterById(99L)).thenReturn(Optional.empty());

            ResponseEntity<Recruiter> response = recruiterController.getRecruiterById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /recruiter/registerRecruiter")
    class RegisterRecruiterTests {

        @Test
        @DisplayName("Should register recruiter successfully")
        void shouldRegisterRecruiterSuccessfully() {
            Map<String, String> request = Map.of(
                    "email", "new@test.com", "password", "pass123",
                    "name", "Doe", "firstName", "Jane", "phone", "0123456789");

            when(recruiterService.registerRecruiter("new@test.com", "pass123", "Doe", "Jane", "0123456789"))
                    .thenReturn(testRecruiter);

            ResponseEntity<?> response = recruiterController.registerRecruiter(request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 401 when registration fails")
        void shouldReturn401WhenRegistrationFails() {
            Map<String, String> request = Map.of(
                    "email", "existing@test.com", "password", "pass123",
                    "name", "Doe", "firstName", "Jane", "phone", "0123456789");

            when(recruiterService.registerRecruiter(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenThrow(new RuntimeException("Email already used"));

            ResponseEntity<?> response = recruiterController.registerRecruiter(request);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /recruiter/addRecruiter2")
    class AddRecruiterWithPictureTests {

        @Test
        @DisplayName("Should add recruiter with picture successfully")
        void shouldAddRecruiterWithPictureSuccessfully() {
            MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());
            when(recruiterService.addRecruiterWithPicture(any(Recruiter.class), eq(imageFile))).thenReturn(testRecruiter);

            ResponseEntity<Recruiter> response = recruiterController.addRecruiterWithPicture(new Recruiter(), imageFile);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /recruiter/{id}")
    class UpdateRecruiterTests {

        @Test
        @DisplayName("Should update recruiter successfully")
        void shouldUpdateRecruiterSuccessfully() {
            when(recruiterService.updateRecruiter(1L, testRecruiter)).thenReturn(testRecruiter);

            ResponseEntity<Recruiter> response = recruiterController.updateRecruiter(1L, testRecruiter);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(recruiterService.updateRecruiter(99L, testRecruiter)).thenThrow(new RuntimeException("Recruiter not found"));

            ResponseEntity<Recruiter> response = recruiterController.updateRecruiter(99L, testRecruiter);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /recruiter/{id}")
    class DeleteRecruiterTests {

        @Test
        @DisplayName("Should delete recruiter successfully")
        void shouldDeleteRecruiterSuccessfully() {
            doNothing().when(recruiterService).deleteRecruiter(1L);

            ResponseEntity<String> response = recruiterController.deleteRecruiter(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Recruiter deleted successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Recruiter not found")).when(recruiterService).deleteRecruiter(99L);

            ResponseEntity<String> response = recruiterController.deleteRecruiter(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /recruiter/files/{filename}")
    class GetFileTests {

        @Test
        @DisplayName("Should return file resource")
        void shouldReturnFileResource() {
            Resource mockResource = mock(Resource.class);
            when(recruiterService.getFile("test.jpg")).thenReturn(ResponseEntity.ok(mockResource));

            ResponseEntity<Resource> response = recruiterController.getFile("test.jpg");

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }
}
