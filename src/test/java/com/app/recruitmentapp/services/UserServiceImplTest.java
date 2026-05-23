package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.entities.User;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private TokenBlacklistService tokenBlacklistService;
    @InjectMocks
    private UserServiceImpl userService;

    private Candidate activeCandidate;
    private Candidate inactiveCandidate;

    @BeforeEach
    void setUp() {
        activeCandidate = new Candidate();
        activeCandidate.setId(1L);
        activeCandidate.setEmail("active@test.com");
        activeCandidate.setName("Doe");
        activeCandidate.setFirstName("John");
        activeCandidate.setPassword("encoded-pass");
        activeCandidate.setRole(Role.CANDIDATE);
        activeCandidate.setActive(true);

        inactiveCandidate = new Candidate();
        inactiveCandidate.setId(2L);
        inactiveCandidate.setEmail("inactive@test.com");
        inactiveCandidate.setName("Smith");
        inactiveCandidate.setFirstName("Jane");
        inactiveCandidate.setPassword("encoded-pass-2");
        inactiveCandidate.setRole(Role.CANDIDATE);
        inactiveCandidate.setActive(false);
    }

    @Nested
    @DisplayName("getAllUsers Tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return all users")
        void shouldGetAllUsersSuccessfully() {
            List<User> mockUsers = List.of(activeCandidate, inactiveCandidate);
            when(userRepository.findAll()).thenReturn(mockUsers);

            List<User> result = userService.getAllUsers();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("active@test.com", result.get(0).getEmail());
            assertEquals("inactive@test.com", result.get(1).getEmail());
            verify(userRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getUserById Tests")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return user when found")
        void shouldReturnUserWhenFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(activeCandidate));

            Optional<User> result = userService.getUserById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("active@test.com", result.get().getEmail());
            verify(userRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<User> result = userService.getUserById(99L);

            assertFalse(result.isPresent());
            verify(userRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("saveUser Tests")
    class SaveUserTests {

        @Test
        @DisplayName("Should save user successfully")
        void shouldSaveUserSuccessfully() {
            User newUser = new User();
            newUser.setEmail("new@test.com");
            newUser.setName("NewUser");
            newUser.setPassword("pass");

            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            User result = userService.saveUser(newUser);

            assertNotNull(result);
            assertEquals("new@test.com", result.getEmail());
            assertEquals("NewUser", result.getName());
            verify(userRepository).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("updateUser Tests")
    class UpdateUserTests {

        @Test
        @DisplayName("Should update user when found")
        void shouldUpdateUserWhenFound() {
            User updatedData = new User();
            updatedData.setName("UpdatedName");
            updatedData.setFirstName("UpdatedFirst");
            updatedData.setEmail("updated@test.com");
            updatedData.setPassword("new-encoded-pass");
            updatedData.setRole(Role.ADMIN);

            when(userRepository.findById(1L)).thenReturn(Optional.of(activeCandidate));
            when(userRepository.saveAndFlush(activeCandidate)).thenReturn(activeCandidate);

            User result = userService.updateUser(1L, updatedData);

            assertNotNull(result);
            assertEquals("UpdatedName", result.getName());
            assertEquals("UpdatedFirst", result.getFirstName());
            assertEquals("updated@test.com", result.getEmail());
            assertEquals("new-encoded-pass", result.getPassword());
            assertEquals(Role.ADMIN, result.getRole());
            verify(userRepository).findById(1L);
            verify(userRepository).saveAndFlush(activeCandidate);
        }

        @Test
        @DisplayName("Should update user with partial fields")
        void shouldUpdateUserWithPartialFields() {
            User updatedData = new User();
            updatedData.setEmail("partial@test.com");

            when(userRepository.findById(1L)).thenReturn(Optional.of(activeCandidate));
            when(userRepository.saveAndFlush(activeCandidate)).thenReturn(activeCandidate);

            User result = userService.updateUser(1L, updatedData);

            assertNotNull(result);
            assertEquals("partial@test.com", result.getEmail());
            assertEquals("Doe", result.getName());
            assertEquals("John", result.getFirstName());
            verify(userRepository).findById(1L);
            verify(userRepository).saveAndFlush(activeCandidate);
        }

        @Test
        @DisplayName("Should return null when user not found")
        void shouldReturnNullWhenUserNotFound() {
            User updatedData = new User();
            updatedData.setName("Any");

            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            User result = userService.updateUser(99L, updatedData);

            assertNull(result);
            verify(userRepository).findById(99L);
            verify(userRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("deleteUser Tests")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user when found")
        void shouldDeleteUserWhenFound() {
            when(userRepository.existsById(1L)).thenReturn(true);

            userService.deleteUser(1L);

            verify(userRepository).existsById(1L);
            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> userService.deleteUser(99L));
            verify(userRepository).existsById(99L);
            verify(userRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with active candidate")
        void shouldLoginSuccessfully() {
            when(userRepository.findByEmail("active@test.com")).thenReturn(Optional.of(activeCandidate));
            when(passwordEncoder.matches("rawPass", "encoded-pass")).thenReturn(true);
            when(jwtUtil.generateToken(eq("active@test.com"), eq(List.of("ROLE_CANDIDATE"))))
                    .thenReturn("generated-jwt-token");

            Map<String, Object> result = userService.login("active@test.com", "rawPass");

            assertNotNull(result);
            assertEquals("generated-jwt-token", result.get("token"));
            assertEquals(activeCandidate, result.get("user"));
            verify(userRepository).findByEmail("active@test.com");
            verify(passwordEncoder).matches("rawPass", "encoded-pass");
            verify(jwtUtil).generateToken("active@test.com", List.of("ROLE_CANDIDATE"));
        }

        @Test
        @DisplayName("Should throw RuntimeException when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> userService.login("unknown@test.com", "pass"));
            verify(userRepository).findByEmail("unknown@test.com");
            verify(passwordEncoder, never()).matches(any(), any());
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("Should throw RuntimeException when password is incorrect")
        void shouldThrowWhenPasswordIncorrect() {
            when(userRepository.findByEmail("active@test.com")).thenReturn(Optional.of(activeCandidate));
            when(passwordEncoder.matches("wrongPass", "encoded-pass")).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> userService.login("active@test.com", "wrongPass"));
            verify(userRepository).findByEmail("active@test.com");
            verify(passwordEncoder).matches("wrongPass", "encoded-pass");
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("Should throw RuntimeException when account is disabled")
        void shouldThrowWhenAccountDisabled() {
            when(userRepository.findByEmail("inactive@test.com")).thenReturn(Optional.of(inactiveCandidate));
            when(passwordEncoder.matches("rawPass", "encoded-pass-2")).thenReturn(true);

            assertThrows(RuntimeException.class,
                    () -> userService.login("inactive@test.com", "rawPass"));
            verify(userRepository).findByEmail("inactive@test.com");
            verify(passwordEncoder).matches("rawPass", "encoded-pass-2");
            verifyNoInteractions(jwtUtil);
        }
    }

    @Nested
    @DisplayName("logout Tests")
    class LogoutTests {

        @Test
        @DisplayName("Should blacklist token on logout")
        void shouldBlacklistTokenOnLogout() {
            String token = "valid-jwt-token";
            Date expirationDate = Date.from(
                    LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
            when(jwtUtil.extractExpiration(token)).thenReturn(expirationDate);

            userService.logout(token);

            verify(jwtUtil).extractExpiration(token);
            verify(tokenBlacklistService).blacklistToken(eq(token), any(LocalDateTime.class));
        }

        @Test
        @DisplayName("Should not blacklist when token is null")
        void shouldNotBlacklistWhenTokenIsNull() {
            userService.logout(null);

            verifyNoInteractions(jwtUtil, tokenBlacklistService);
        }
    }
}
