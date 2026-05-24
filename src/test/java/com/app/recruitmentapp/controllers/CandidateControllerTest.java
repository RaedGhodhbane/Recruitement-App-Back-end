package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.CandidateDTO;
import com.app.recruitmentapp.entities.ChangePassword;
import com.app.recruitmentapp.services.CandidateService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CandidateController Unit Tests")
class CandidateControllerTest {

    @Mock
    private CandidateService candidateService;

    @InjectMocks
    private CandidateController candidateController;

    private CandidateDTO testCandidateDTO;

    @BeforeEach
    void setUp() {
        testCandidateDTO = new CandidateDTO();
        testCandidateDTO.setId(1L);
        testCandidateDTO.setEmail("candidate@test.com");
        testCandidateDTO.setName("Doe");
        testCandidateDTO.setFirstName("John");
        testCandidateDTO.setPhone("0123456789");
    }

    @Nested
    @DisplayName("GET /candidate/candidates")
    class GetAllCandidatesTests {

        @Test
        @DisplayName("Should return all candidates")
        void shouldReturnAllCandidates() {
            when(candidateService.getAllCandidates()).thenReturn(List.of(testCandidateDTO));

            List<CandidateDTO> result = candidateController.getAllCandidates();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /candidate/{id}")
    class GetCandidateByIdTests {

        @Test
        @DisplayName("Should return candidate when found")
        void shouldReturnCandidateWhenFound() {
            when(candidateService.getCandidateById(1L)).thenReturn(Optional.of(testCandidateDTO));

            ResponseEntity<CandidateDTO> response = candidateController.getCandidateById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("candidate@test.com", response.getBody().getEmail());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(candidateService.getCandidateById(99L)).thenReturn(Optional.empty());

            ResponseEntity<CandidateDTO> response = candidateController.getCandidateById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /candidate/registerCandidate")
    class RegisterCandidateTests {

        @Test
        @DisplayName("Should register candidate successfully")
        void shouldRegisterCandidateSuccessfully() {
            Map<String, String> request = Map.of(
                    "email", "new@test.com", "password", "pass123",
                    "name", "Doe", "firstName", "John", "phone", "0123456789");

            when(candidateService.registerCandidate("new@test.com", "pass123", "Doe", "John", "0123456789"))
                    .thenReturn(testCandidateDTO);

            ResponseEntity<?> response = candidateController.registerCandidate(request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 401 when registration fails")
        void shouldReturn401WhenRegistrationFails() {
            Map<String, String> request = Map.of(
                    "email", "existing@test.com", "password", "pass123",
                    "name", "Doe", "firstName", "John", "phone", "0123456789");

            when(candidateService.registerCandidate(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenThrow(new RuntimeException("Email already used"));

            ResponseEntity<?> response = candidateController.registerCandidate(request);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /candidate/addCandidate2")
    class AddCandidateWithPictureTests {

        @Test
        @DisplayName("Should add candidate with picture successfully")
        void shouldAddCandidateWithPictureSuccessfully() throws Exception {
            MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());
            when(candidateService.saveCandidateWithPicture(any(CandidateDTO.class), eq(imageFile))).thenReturn(testCandidateDTO);

            ResponseEntity<CandidateDTO> response = candidateController.addCandidateWithPicture(new CandidateDTO(), imageFile);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /candidate/update/{id}")
    class UpdateCandidateTests {

        @Test
        @DisplayName("Should update candidate successfully")
        void shouldUpdateCandidateSuccessfully() {
            when(candidateService.updateCandidate(1L, testCandidateDTO)).thenReturn(testCandidateDTO);

            ResponseEntity<CandidateDTO> response = candidateController.updateCandidate(1L, testCandidateDTO);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(candidateService.updateCandidate(99L, testCandidateDTO)).thenThrow(new RuntimeException("Candidate not found"));

            ResponseEntity<CandidateDTO> response = candidateController.updateCandidate(99L, testCandidateDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /candidate/{id}")
    class DeleteCandidateTests {

        @Test
        @DisplayName("Should delete candidate successfully")
        void shouldDeleteCandidateSuccessfully() {
            doNothing().when(candidateService).deleteCandidate(1L);

            ResponseEntity<String> response = candidateController.deleteCandidate(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Candidate deleted successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Candidate not found")).when(candidateService).deleteCandidate(99L);

            ResponseEntity<String> response = candidateController.deleteCandidate(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /candidate/{id}/cv")
    class CreateCVTests {

        @Test
        @DisplayName("Should upload CV successfully")
        void shouldUploadCVSuccessfully() throws Exception {
            MultipartFile file = new MockMultipartFile("file", "cv.pdf", "application/pdf", "cv".getBytes());
            doNothing().when(candidateService).createCV(1L, file);

            ResponseEntity<String> response = candidateController.createCV(1L, file);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("CV uploaded successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 500 when CV upload fails")
        void shouldReturn500WhenCvUploadFails() throws Exception {
            MultipartFile file = new MockMultipartFile("file", "cv.pdf", "application/pdf", "cv".getBytes());
            doThrow(new RuntimeException("Error")).when(candidateService).createCV(1L, file);

            ResponseEntity<String> response = candidateController.createCV(1L, file);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /candidate/{id}/cv")
    class DownloadCVPDFTests {

        @Test
        @DisplayName("Should download CV successfully")
        void shouldDownloadCVSuccessfully() throws Exception {
            byte[] cvData = "PDF content".getBytes();
            when(candidateService.downloadCVPDF(1L)).thenReturn(cvData);

            ResponseEntity<byte[]> response = candidateController.downloadCVPDF(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertArrayEquals(cvData, response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when CV not found")
        void shouldReturn404WhenCvNotFound() throws Exception {
            when(candidateService.downloadCVPDF(99L)).thenThrow(new RuntimeException("CV not found"));

            ResponseEntity<byte[]> response = candidateController.downloadCVPDF(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /candidate/files/{filename}")
    class GetFileTests {

        @Test
        @DisplayName("Should return file resource")
        void shouldReturnFileResource() {
            Resource mockResource = mock(Resource.class);
            when(candidateService.getFile("test.jpg")).thenReturn(ResponseEntity.ok(mockResource));

            ResponseEntity<Resource> response = candidateController.getFile("test.jpg");

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /candidate/{id}/change-password")
    class ChangePasswordTests {

        @Test
        @DisplayName("Should change password successfully")
        void shouldChangePasswordSuccessfully() {
            ChangePassword changePassword = new ChangePassword();
            when(candidateService.changePassword(1L, changePassword)).thenReturn("Password changed successfully");

            ResponseEntity<Map<String, String>> response = candidateController.changePassword(1L, changePassword);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Password changed successfully", response.getBody().get("message"));
        }

        @Test
        @DisplayName("Should return 400 when old password is incorrect")
        void shouldReturn400WhenOldPasswordIncorrect() {
            ChangePassword changePassword = new ChangePassword();
            when(candidateService.changePassword(1L, changePassword)).thenThrow(new IllegalArgumentException("Old password is incorrect"));

            ResponseEntity<Map<String, String>> response = candidateController.changePassword(1L, changePassword);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when candidate not found")
        void shouldReturn404WhenCandidateNotFound() {
            ChangePassword changePassword = new ChangePassword();
            when(candidateService.changePassword(99L, changePassword)).thenThrow(new RuntimeException("Candidate not found"));

            ResponseEntity<Map<String, String>> response = candidateController.changePassword(99L, changePassword);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
