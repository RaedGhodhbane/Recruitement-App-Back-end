package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OfferServiceImpl Unit Tests")
class OfferServiceImplTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private RecruiterRepository recruiterRepository;
    @InjectMocks
    private OfferServiceImpl offerService;

    private Recruiter recruiter;
    private Offer offer1;
    private Offer offer2;

    @BeforeEach
    void setUp() {
        recruiter = new Recruiter();
        recruiter.setId(1L);
        recruiter.setEmail("recruiter@test.com");

        offer1 = new Offer();
        offer1.setId(1L);
        offer1.setTitle("Software Engineer");
        offer1.setDescription("Développement backend");
        offer1.setType("CDI");
        offer1.setAddress("Paris");
        offer1.setSalary(50000);
        offer1.setExperience("3-5 ans");
        offer1.setPublicationDate(LocalDate.of(2026, 1, 15));
        offer1.setExpirationDate(LocalDate.of(2026, 6, 15));
        offer1.setRecruiter(recruiter);

        offer2 = new Offer();
        offer2.setId(2L);
        offer2.setTitle("Data Scientist");
        offer2.setDescription("Analyse de données");
        offer2.setType("CDD");
        offer2.setAddress("Lyon");
        offer2.setSalary(45000);
        offer2.setExperience("2-3 ans");
        offer2.setPublicationDate(LocalDate.of(2026, 2, 1));
        offer2.setExpirationDate(LocalDate.of(2026, 8, 1));
        offer2.setRecruiter(recruiter);
    }

    @Nested
    @DisplayName("getAllOffers Tests")
    class GetAllOffersTests {

        @Test
        @DisplayName("Should return all offers")
        void shouldGetAllOffersSuccessfully() {
            List<Offer> mockOffers = List.of(offer1, offer2);
            when(offerRepository.findAll()).thenReturn(mockOffers);

            List<Offer> result = offerService.getAllOffers();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Software Engineer", result.get(0).getTitle());
            assertEquals("Data Scientist", result.get(1).getTitle());
            verify(offerRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getOfferById Tests")
    class GetOfferByIdTests {

        @Test
        @DisplayName("Should return offer when found")
        void shouldReturnOfferWhenFound() {
            when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));

            Optional<Offer> result = offerService.getOfferById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("CDI", result.get().getType());
            assertEquals(50000, result.get().getSalary());
            verify(offerRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(offerRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Offer> result = offerService.getOfferById(99L);

            assertFalse(result.isPresent());
            verify(offerRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("saveOffer Tests")
    class SaveOfferTests {

        @Test
        @DisplayName("Should save offer successfully")
        void shouldSaveOfferSuccessfully() {
            Offer newOffer = new Offer();
            newOffer.setTitle("DevOps Engineer");
            newOffer.setDescription("Infrastructure et CI/CD");
            newOffer.setType("CDI");
            newOffer.setAddress("Toulouse");
            newOffer.setSalary(55000);
            newOffer.setExperience("4-6 ans");

            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));
            when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> {
                Offer saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Offer result = offerService.saveOffer(newOffer, 1L);

            assertNotNull(result);
            assertEquals("DevOps Engineer", result.getTitle());
            assertEquals(recruiter, result.getRecruiter());
            verify(recruiterRepository).findById(1L);
            verify(offerRepository).save(any(Offer.class));
        }

        @Test
        @DisplayName("Should save offer with null recruiter when not found")
        void shouldSaveOfferWithNullRecruiterWhenNotFound() {
            Offer newOffer = new Offer();
            newOffer.setTitle("DevOps Engineer");
            newOffer.setDescription("Infrastructure");

            when(recruiterRepository.findById(99L)).thenReturn(Optional.empty());
            when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> {
                Offer saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Offer result = offerService.saveOffer(newOffer, 99L);

            assertNotNull(result);
            assertNull(result.getRecruiter());
            verify(recruiterRepository).findById(99L);
            verify(offerRepository).save(any(Offer.class));
        }
    }

    @Nested
    @DisplayName("updateOffer Tests")
    class UpdateOfferTests {

        @Test
        @DisplayName("Should update offer when found")
        void shouldUpdateOfferWhenFound() {
            Offer updatedData = new Offer();
            updatedData.setTitle("Senior Engineer");
            updatedData.setDescription("Lead developer");
            updatedData.setType("CDI");
            updatedData.setAddress("Lyon");
            updatedData.setSalary(65000);
            updatedData.setExperience("7+ ans");
            updatedData.setPublicationDate(LocalDate.of(2026, 3, 1));
            updatedData.setExpirationDate(LocalDate.of(2026, 9, 1));

            when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));
            when(offerRepository.saveAndFlush(offer1)).thenReturn(offer1);

            Offer result = offerService.updateOffer(1L, updatedData);

            assertNotNull(result);
            assertEquals("Senior Engineer", result.getTitle());
            assertEquals("Lead developer", result.getDescription());
            assertEquals("CDI", result.getType());
            assertEquals("Lyon", result.getAddress());
            assertEquals(65000, result.getSalary());
            assertEquals("7+ ans", result.getExperience());
            assertEquals(LocalDate.of(2026, 3, 1), result.getPublicationDate());
            assertEquals(LocalDate.of(2026, 9, 1), result.getExpirationDate());
            verify(offerRepository).findById(1L);
            verify(offerRepository).saveAndFlush(offer1);
        }

        @Test
        @DisplayName("Should throw NullPointerException when offer not found")
        void shouldThrowWhenOfferNotFound() {
            Offer updatedData = new Offer();
            updatedData.setTitle("Senior Engineer");

            when(offerRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(NullPointerException.class,
                    () -> offerService.updateOffer(99L, updatedData));
            verify(offerRepository).findById(99L);
            verify(offerRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("deleteOffer Tests")
    class DeleteOfferTests {

        @Test
        @DisplayName("Should delete offer when found")
        void shouldDeleteOfferWhenFound() {
            when(offerRepository.existsById(1L)).thenReturn(true);

            offerService.deleteOffer(1L);

            verify(offerRepository).existsById(1L);
            verify(offerRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenOfferNotFound() {
            when(offerRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> offerService.deleteOffer(99L));
            verify(offerRepository).existsById(99L);
            verify(offerRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("deleteOfferExpired Tests")
    class DeleteOfferExpiredTests {

        @Test
        @DisplayName("Should delete expired offers")
        void shouldDeleteExpiredOffers() {
            offerService.deleteOfferExpired();

            verify(offerRepository).deleteByExpirationDateBefore(any(LocalDate.class));
        }
    }
}
