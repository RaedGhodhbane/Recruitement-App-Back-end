package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Favourite;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.FavouriteRepository;
import org.junit.jupiter.api.BeforeEach;
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
@DisplayName("FavouriteServiceImpl Unit Tests")
class FavouriteServiceImplTest {

    @Mock
    private FavouriteRepository favouriteRepository;
    @InjectMocks
    private FavouriteServiceImpl favouriteService;

    private User user;
    private Offer offer;
    private Favourite favourite;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        offer = new Offer();
        offer.setId(10L);
        offer.setTitle("Software Engineer");

        favourite = new Favourite();
        favourite.setId(100L);
        favourite.setUser(user);
        favourite.setOffer(offer);
    }

    @Nested
    @DisplayName("saveJob Tests")
    class SaveJobTests {

        @Test
        @DisplayName("Should save job when not already saved")
        void shouldSaveJobWhenNotAlreadySaved() {
            when(favouriteRepository.existsByUserIdAndOfferId(1L, 10L)).thenReturn(false);
            when(favouriteRepository.save(any(Favourite.class))).thenReturn(favourite);

            Favourite result = favouriteService.saveJob(user, offer);

            assertNotNull(result);
            assertEquals(100L, result.getId());
            assertEquals(user, result.getUser());
            assertEquals(offer, result.getOffer());
            verify(favouriteRepository).existsByUserIdAndOfferId(1L, 10L);
            verify(favouriteRepository).save(any(Favourite.class));
        }

        @Test
        @DisplayName("Should throw RuntimeException when job already saved")
        void shouldThrowWhenJobAlreadySaved() {
            when(favouriteRepository.existsByUserIdAndOfferId(1L, 10L)).thenReturn(true);

            assertThrows(RuntimeException.class,
                    () -> favouriteService.saveJob(user, offer));
            verify(favouriteRepository).existsByUserIdAndOfferId(1L, 10L);
            verify(favouriteRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getSavedJobs Tests")
    class GetSavedJobsTests {

        @Test
        @DisplayName("Should return saved jobs for user")
        void shouldReturnSavedJobsForUser() {
            List<Favourite> mockFavourites = List.of(favourite);
            when(favouriteRepository.findByUserId(1L)).thenReturn(mockFavourites);

            List<Favourite> result = favouriteService.getSavedJobs(1L);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(100L, result.get(0).getId());
            verify(favouriteRepository).findByUserId(1L);
        }

        @Test
        @DisplayName("Should return empty list when no saved jobs")
        void shouldReturnEmptyListWhenNoSavedJobs() {
            when(favouriteRepository.findByUserId(2L)).thenReturn(List.of());

            List<Favourite> result = favouriteService.getSavedJobs(2L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(favouriteRepository).findByUserId(2L);
        }
    }

    @Nested
    @DisplayName("removeSavedJob Tests")
    class RemoveSavedJobTests {

        @Test
        @DisplayName("Should remove saved job by id")
        void shouldRemoveSavedJobById() {
            favouriteService.removeSavedJob(100L);

            verify(favouriteRepository).deleteById(100L);
        }
    }
}
