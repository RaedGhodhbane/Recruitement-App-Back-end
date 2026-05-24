package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.FavouriteDTO;
import com.app.recruitmentapp.entities.Favourite;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.repositories.FavouriteRepository;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
@DisplayName("FavouriteServiceImpl Unit Tests")
class FavouriteServiceImplTest {

    @Mock
    private FavouriteRepository favouriteRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private FavouriteServiceImpl favouriteService;

    private User user;
    private Offer offer;
    private Favourite favourite;
    private FavouriteDTO favouriteDTO;

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

        favouriteDTO = new FavouriteDTO();
        favouriteDTO.setId(100L);
        favouriteDTO.setUserId(1L);
        favouriteDTO.setOfferId(10L);
    }

    @Nested
    @DisplayName("saveJob Tests")
    class SaveJobTests {

        @Test
        @DisplayName("Should save job when not already saved")
        void shouldSaveJobWhenNotAlreadySaved() {
            Favourite savedEntity = new Favourite();
            savedEntity.setId(100L);
            savedEntity.setUser(user);
            savedEntity.setOffer(offer);

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(offerRepository.findById(10L)).thenReturn(Optional.of(offer));
            when(favouriteRepository.existsByUserIdAndOfferId(1L, 10L)).thenReturn(false);
            when(favouriteRepository.save(any(Favourite.class))).thenReturn(savedEntity);
            when(entityMapper.toFavouriteDTO(any(Favourite.class))).thenReturn(favouriteDTO);

            FavouriteDTO result = favouriteService.saveJob(1L, 10L);

            assertNotNull(result);
            assertEquals(100L, result.getId());
            assertEquals(1L, result.getUserId());
            assertEquals(10L, result.getOfferId());
            verify(userRepository).findById(1L);
            verify(offerRepository).findById(10L);
            verify(favouriteRepository).existsByUserIdAndOfferId(1L, 10L);
            verify(favouriteRepository).save(any(Favourite.class));
            verify(entityMapper).toFavouriteDTO(any(Favourite.class));
        }

        @Test
        @DisplayName("Should throw RuntimeException when job already saved")
        void shouldThrowWhenJobAlreadySaved() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(offerRepository.findById(10L)).thenReturn(Optional.of(offer));
            when(favouriteRepository.existsByUserIdAndOfferId(1L, 10L)).thenReturn(true);

            assertThrows(RuntimeException.class,
                    () -> favouriteService.saveJob(1L, 10L));
            verify(userRepository).findById(1L);
            verify(offerRepository).findById(10L);
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
            when(entityMapper.toFavouriteDTOList(mockFavourites)).thenReturn(List.of(favouriteDTO));

            List<FavouriteDTO> result = favouriteService.getSavedJobs(1L);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(100L, result.get(0).getId());
            verify(favouriteRepository).findByUserId(1L);
            verify(entityMapper).toFavouriteDTOList(mockFavourites);
        }

        @Test
        @DisplayName("Should return empty list when no saved jobs")
        void shouldReturnEmptyListWhenNoSavedJobs() {
            when(favouriteRepository.findByUserId(2L)).thenReturn(List.of());
            when(entityMapper.toFavouriteDTOList(List.of())).thenReturn(List.of());

            List<FavouriteDTO> result = favouriteService.getSavedJobs(2L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(favouriteRepository).findByUserId(2L);
            verify(entityMapper).toFavouriteDTOList(List.of());
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
