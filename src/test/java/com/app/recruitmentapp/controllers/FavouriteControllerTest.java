package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.FavouriteDTO;
import com.app.recruitmentapp.services.FavouriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavouriteController Unit Tests")
class FavouriteControllerTest {

    @Mock
    private FavouriteService favouriteService;

    @InjectMocks
    private FavouriteController favouriteController;

    @Nested
    @DisplayName("POST /api/saved-jobs/{userId}/{offerId}")
    class FavouriteTests {

        @Test
        @DisplayName("Should save job as favourite successfully")
        void shouldSaveJobSuccessfully() {
            when(favouriteService.saveJob(1L, 1L)).thenReturn(new FavouriteDTO());

            FavouriteDTO result = favouriteController.favourite(1L, 1L);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(favouriteService.saveJob(99L, 1L)).thenThrow(new RuntimeException("User not found"));

            assertThrows(RuntimeException.class, () -> favouriteController.favourite(99L, 1L));
        }
    }

    @Nested
    @DisplayName("GET /api/saved-jobs/{userId}")
    class GetSavedJobsTests {

        @Test
        @DisplayName("Should return saved jobs for user")
        void shouldReturnSavedJobs() {
            when(favouriteService.getSavedJobs(1L)).thenReturn(List.of(new FavouriteDTO()));

            List<FavouriteDTO> result = favouriteController.getSavedJobs(1L);

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("DELETE /api/saved-jobs/{savedJobId}")
    class RemoveSavedJobTests {

        @Test
        @DisplayName("Should remove saved job successfully")
        void shouldRemoveSavedJobSuccessfully() {
            doNothing().when(favouriteService).removeSavedJob(1L);

            assertDoesNotThrow(() -> favouriteController.removeSavedJob(1L));
            verify(favouriteService).removeSavedJob(1L);
        }
    }
}
