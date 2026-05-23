package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Unit Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@test.com");
    }

    @Nested
    @DisplayName("GET /admin/user/users")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return all users")
        void shouldReturnAllUsers() {
            when(userService.getAllUsers()).thenReturn(List.of(testUser));

            List<User> result = userController.getAllUsers();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /admin/user/{id}")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return user when found")
        void shouldReturnUserWhenFound() {
            when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

            ResponseEntity<User> response = userController.getUserById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(userService.getUserById(99L)).thenReturn(Optional.empty());

            ResponseEntity<User> response = userController.getUserById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /admin/user")
    class AddUserTests {

        @Test
        @DisplayName("Should add user successfully")
        void shouldAddUserSuccessfully() {
            when(userService.saveUser(testUser)).thenReturn(testUser);

            ResponseEntity<User> response = userController.addUser(testUser);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /admin/user/{id}")
    class UpdateUserTests {

        @Test
        @DisplayName("Should update user successfully")
        void shouldUpdateUserSuccessfully() {
            when(userService.updateUser(1L, testUser)).thenReturn(testUser);

            ResponseEntity<User> response = userController.updateUser(1L, testUser);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(userService.updateUser(99L, testUser)).thenThrow(new RuntimeException("User not found"));

            ResponseEntity<User> response = userController.updateUser(99L, testUser);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /admin/user/{id}")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user successfully")
        void shouldDeleteUserSuccessfully() {
            doNothing().when(userService).deleteUser(1L);

            ResponseEntity<String> response = userController.deleteUser(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("User deleted successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("User not found")).when(userService).deleteUser(99L);

            ResponseEntity<String> response = userController.deleteUser(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
