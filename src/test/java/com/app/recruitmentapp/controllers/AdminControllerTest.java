package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.AdminDTO;
import com.app.recruitmentapp.dto.ContactDTO;
import com.app.recruitmentapp.entities.ChangePassword;
import com.app.recruitmentapp.services.AdminService;
import com.app.recruitmentapp.services.ContactService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminController Unit Tests")
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private AdminController adminController;

    private AdminDTO testAdminDTO;

    @BeforeEach
    void setUp() {
        testAdminDTO = new AdminDTO();
        testAdminDTO.setId(1L);
        testAdminDTO.setEmail("admin@test.com");
        testAdminDTO.setName("Ghodhbane");
        testAdminDTO.setFirstName("Raed");
        testAdminDTO.setCity("Paris");
        testAdminDTO.setCountry("France");
        testAdminDTO.setActive(true);
    }

    @Nested
    @DisplayName("GET /admin/admins")
    class GetAllAdminsTests {

        @Test
        @DisplayName("Should return all admins successfully")
        void shouldGetAllAdminsSuccessfully() {
            AdminDTO admin2 = new AdminDTO();
            admin2.setId(2L);
            admin2.setEmail("admin2@test.com");
            List<AdminDTO> mockAdmins = List.of(testAdminDTO, admin2);
            when(adminService.getAllAdmins()).thenReturn(mockAdmins);

            List<AdminDTO> result = adminController.getAllAdmins();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("admin@test.com", result.get(0).getEmail());
        }
    }

    @Nested
    @DisplayName("GET /admin/{id}")
    class GetAdminByIdTests {

        @Test
        @DisplayName("Should return admin when found")
        void shouldReturnAdminWhenFound() {
            when(adminService.getAdminById(1L)).thenReturn(Optional.of(testAdminDTO));

            ResponseEntity<AdminDTO> response = adminController.getAdminById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("admin@test.com", response.getBody().getEmail());
        }

        @Test
        @DisplayName("Should return 404 when admin not found")
        void shouldReturn404WhenNotFound() {
            when(adminService.getAdminById(99L)).thenReturn(Optional.empty());

            ResponseEntity<AdminDTO> response = adminController.getAdminById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /admin/registerAdmin")
    class RegisterAdminWithPictureTests {

        @Test
        @DisplayName("Should register admin successfully")
        void shouldRegisterAdminSuccessfully() {
            Map<String, String> request = Map.of("email", "newadmin@test.com", "password", "password123");
            MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());

            when(adminService.registerAdminWithPicture(eq("newadmin@test.com"), eq("password123"), any(AdminDTO.class), eq(imageFile)))
                    .thenReturn(testAdminDTO);

            ResponseEntity<?> response = adminController.registerAdminWithPicture(request, new AdminDTO(), imageFile);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Should return 401 when registration fails")
        void shouldReturn401WhenRegistrationFails() {
            Map<String, String> request = Map.of("email", "existing@test.com", "password", "password123");
            MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());

            when(adminService.registerAdminWithPicture(anyString(), anyString(), any(AdminDTO.class), any(MultipartFile.class)))
                    .thenThrow(new RuntimeException("Email already used"));

            ResponseEntity<?> response = adminController.registerAdminWithPicture(request, new AdminDTO(), imageFile);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("Email already used"));
        }
    }

    @Nested
    @DisplayName("PUT /admin/updateadmin/{id}")
    class UpdateAdminTests {

        @Test
        @DisplayName("Should update admin successfully")
        void shouldUpdateAdminSuccessfully() {
            MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());
            when(adminService.updateAdmin(eq(1L), any(AdminDTO.class), eq(imageFile))).thenReturn(testAdminDTO);

            ResponseEntity<AdminDTO> response = adminController.updateAdmin(1L, new AdminDTO(), imageFile);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());
            when(adminService.updateAdmin(anyLong(), any(AdminDTO.class), any(MultipartFile.class)))
                    .thenThrow(new RuntimeException("Admin not found"));

            ResponseEntity<AdminDTO> response = adminController.updateAdmin(99L, new AdminDTO(), imageFile);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /admin/{id}")
    class DeleteAdminTests {

        @Test
        @DisplayName("Should delete admin successfully")
        void shouldDeleteAdminSuccessfully() {
            doNothing().when(adminService).deleteAdmin(1L);

            ResponseEntity<String> response = adminController.deleteAdmin(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Admin deleted successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Admin not found")).when(adminService).deleteAdmin(99L);

            ResponseEntity<String> response = adminController.deleteAdmin(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /admin/recruiters/{id}/activate")
    class ActivateRecruiterTests {

        @Test
        @DisplayName("Should activate recruiter successfully")
        void shouldActivateRecruiterSuccessfully() {
            doNothing().when(adminService).activateRecruiterAccount(1L);

            ResponseEntity<Void> response = adminController.activateRecruiter(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /admin/recruiters/{id}/desactivate")
    class DesactivateRecruiterTests {

        @Test
        @DisplayName("Should desactivate recruiter successfully")
        void shouldDesactivateRecruiterSuccessfully() {
            doNothing().when(adminService).desactivateRecruiterAccount(1L);

            ResponseEntity<Void> response = adminController.desactivateRecruiter(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /admin/candidates/{id}/activate")
    class ActivateCandidateTests {

        @Test
        @DisplayName("Should activate candidate successfully")
        void shouldActivateCandidateSuccessfully() {
            doNothing().when(adminService).activateCandidateAccount(1L);

            ResponseEntity<Void> response = adminController.activateCandidate(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /admin/candidates/{id}/desactivate")
    class DesactivateCandidateTests {

        @Test
        @DisplayName("Should desactivate candidate successfully")
        void shouldDesactivateCandidateSuccessfully() {
            doNothing().when(adminService).desactivateCandidateAccount(1L);

            ResponseEntity<Void> response = adminController.desactivateCandidate(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /admin/{id}/change-password")
    class ChangePasswordTests {

        @Test
        @DisplayName("Should change password successfully")
        void shouldChangePasswordSuccessfully() {
            ChangePassword changePassword = new ChangePassword();
            when(adminService.changePassword(1L, changePassword)).thenReturn("Password changed successfully");

            ResponseEntity<Map<String, String>> response = adminController.changePassword(1L, changePassword);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Password changed successfully", response.getBody().get("message"));
        }

        @Test
        @DisplayName("Should return 400 when old password is incorrect")
        void shouldReturn400WhenOldPasswordIncorrect() {
            ChangePassword changePassword = new ChangePassword();
            when(adminService.changePassword(1L, changePassword)).thenThrow(new IllegalArgumentException("Old password is incorrect"));

            ResponseEntity<Map<String, String>> response = adminController.changePassword(1L, changePassword);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().containsKey("erreur"));
        }

        @Test
        @DisplayName("Should return 404 when admin not found")
        void shouldReturn404WhenAdminNotFound() {
            ChangePassword changePassword = new ChangePassword();
            when(adminService.changePassword(99L, changePassword)).thenThrow(new RuntimeException("Admin not found"));

            ResponseEntity<Map<String, String>> response = adminController.changePassword(99L, changePassword);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertTrue(response.getBody().containsKey("erreur"));
        }
    }

    @Nested
    @DisplayName("GET /admin/files/{filename}")
    class GetFileTests {

        @Test
        @DisplayName("Should return file resource")
        void shouldReturnFileResource() {
            Resource mockResource = mock(Resource.class);
            when(adminService.getFile("test.jpg")).thenReturn(ResponseEntity.ok(mockResource));

            ResponseEntity<Resource> response = adminController.getFile("test.jpg");

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /admin/contact/messages")
    class GetAllMessagesContactTests {

        @Test
        @DisplayName("Should return all contact messages")
        void shouldReturnAllContactMessages() {
            ContactDTO contact1 = new ContactDTO();
            ContactDTO contact2 = new ContactDTO();
            when(contactService.getAllMessagesContact()).thenReturn(List.of(contact1, contact2));

            List<ContactDTO> result = adminController.getAllMessagesContact();

            assertNotNull(result);
            assertEquals(2, result.size());
        }
    }
}
