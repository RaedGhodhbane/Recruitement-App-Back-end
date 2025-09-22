package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.repositories.AdminRepository;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("AdminServiceImpl Unit Tests")
class AdminServiceImplTest {

    @Mock
    private RecruiterRepository recruiterRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AdminServiceImpl adminService; // this is the class that we want to test

    private Admin testAdmin1;

    private Admin testAdmin2;

    @BeforeEach
    void setUp() {
        testAdmin1 = new Admin();
        testAdmin1.setId(1L);
        testAdmin1.setEmail("admin1@test.com");
        testAdmin1.setName("Ghodhbane");
        testAdmin1.setFirstName("Raed");
        testAdmin1.setCity("Paris");
        testAdmin1.setCountry("France");
        testAdmin1.setActive(true);

        testAdmin2 = new Admin();
        testAdmin2.setId(2L);
        testAdmin2.setEmail("admin2@test.com");
        testAdmin2.setName("Ghodhbane");
        testAdmin2.setFirstName("Skander");
        testAdmin2.setCity("Tunis");
        testAdmin2.setCountry("Tunisie");
        testAdmin2.setActive(false);
    }

    @Nested
    @DisplayName("Create getAllAdmins Tests")
    class getAllAdminsTests {
        @Test
        @DisplayName("Should create getAllAdmins successfully")
        void shouldGetAllAdminsSuccessfully() {

            // Given : Préparation des mocks
            List<Admin> mockAdmins = List.of(testAdmin1, testAdmin2);
            when(adminRepository.findAll()).thenReturn(mockAdmins);

            // When : Exécution de la méthode à tester
            List<Admin> result = adminService.getAllAdmins();

            //  Then : Vérification des résultats
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("admin1@test.com", result.get(0).getEmail());
            assertEquals("admin2@test.com", result.get(1).getEmail());
            assertTrue(result.get(0).getActive());
            assertFalse(result.get(1).getActive());

        }

    }
}