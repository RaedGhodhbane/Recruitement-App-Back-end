package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Favourite;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import com.app.recruitmentapp.services.FavouriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavouriteController Unit Tests")
class FavouriteControllerTest {

    @Mock
    private FavouriteService favouriteService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private FavouriteController favouriteController;

    @Nested
    @DisplayName("POST /api/saved-jobs/{userId}/{offerId}")
    class FavouriteTests {

        @Test
        @DisplayName("Should save job as favourite successfully")
        void shouldSaveJobSuccessfully() {
            User user = new User();
            user.setId(1L);
            Offer offer = new Offer();
            offer.setId(1L);
            Favourite favourite = new Favourite();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
            when(favouriteService.saveJob(user, offer)).thenReturn(favourite);

            Favourite result = favouriteController.favourite(1L, 1L);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(99L)).thenThrow(new RuntimeException("User not found"));

            assertThrows(RuntimeException.class, () -> favouriteController.favourite(99L, 1L));
        }

        @Test
        @DisplayName("Should throw when offer not found")
        void shouldThrowWhenOfferNotFound() {
            User user = new User();
            user.setId(1L);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(offerRepository.findById(99L)).thenThrow(new RuntimeException("Offer not found"));

            assertThrows(RuntimeException.class, () -> favouriteController.favourite(1L, 99L));
        }
    }

    @Nested
    @DisplayName("GET /api/saved-jobs/{userId}")
    class GetSavedJobsTests {

        @Test
        @DisplayName("Should return saved jobs for user")
        void shouldReturnSavedJobs() {
            List<Favourite> favourites = List.of(new Favourite());
            when(favouriteService.getSavedJobs(1L)).thenReturn(favourites);

            List<Favourite> result = favouriteController.getSavedJobs(1L);

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
