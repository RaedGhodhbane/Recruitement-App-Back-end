package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.OfferDTO;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.mapper.EntityMapper;
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
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private OfferServiceImpl offerService;

    private Recruiter recruiter;
    private Offer offer1;
    private Offer offer2;
    private OfferDTO offer1DTO;
    private OfferDTO offer2DTO;

    @BeforeEach
    void setUp() {
        recruiter = new Recruiter();
        recruiter.setId(1L);
        recruiter.setEmail("recruiter@test.com");

        offer1 = new Offer();
        offer1.setId(1L);
        offer1.setTitle("Software Engineer");
        offer1.setDescription("Developpement backend");
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
        offer2.setDescription("Analyse de donnees");
        offer2.setType("CDD");
        offer2.setAddress("Lyon");
        offer2.setSalary(45000);
        offer2.setExperience("2-3 ans");
        offer2.setPublicationDate(LocalDate.of(2026, 2, 1));
        offer2.setExpirationDate(LocalDate.of(2026, 8, 1));
        offer2.setRecruiter(recruiter);

        offer1DTO = new OfferDTO();
        offer1DTO.setId(1L);
        offer1DTO.setTitle("Software Engineer");
        offer1DTO.setDescription("Developpement backend");
        offer1DTO.setType("CDI");
        offer1DTO.setAddress("Paris");
        offer1DTO.setSalary(50000);
        offer1DTO.setExperience("3-5 ans");
        offer1DTO.setPublicationDate(LocalDate.of(2026, 1, 15));
        offer1DTO.setExpirationDate(LocalDate.of(2026, 6, 15));
        offer1DTO.setRecruiterId(1L);

        offer2DTO = new OfferDTO();
        offer2DTO.setId(2L);
        offer2DTO.setTitle("Data Scientist");
        offer2DTO.setDescription("Analyse de donnees");
        offer2DTO.setType("CDD");
        offer2DTO.setAddress("Lyon");
        offer2DTO.setSalary(45000);
        offer2DTO.setExperience("2-3 ans");
        offer2DTO.setPublicationDate(LocalDate.of(2026, 2, 1));
        offer2DTO.setExpirationDate(LocalDate.of(2026, 8, 1));
        offer2DTO.setRecruiterId(1L);
    }

    @Nested
    @DisplayName("getAllOffers Tests")
    class GetAllOffersTests {

        @Test
        @DisplayName("Should return all offers")
        void shouldGetAllOffersSuccessfully() {
            List<Offer> mockOffers = List.of(offer1, offer2);
            when(offerRepository.findAll()).thenReturn(mockOffers);
            when(entityMapper.toOfferDTOList(mockOffers)).thenReturn(List.of(offer1DTO, offer2DTO));

            List<OfferDTO> result = offerService.getAllOffers();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Software Engineer", result.get(0).getTitle());
            assertEquals("Data Scientist", result.get(1).getTitle());
            verify(offerRepository).findAll();
            verify(entityMapper).toOfferDTOList(mockOffers);
        }
    }

    @Nested
    @DisplayName("getOfferById Tests")
    class GetOfferByIdTests {

        @Test
        @DisplayName("Should return offer when found")
        void shouldReturnOfferWhenFound() {
            when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));
            when(entityMapper.toOfferDTO(offer1)).thenReturn(offer1DTO);

            Optional<OfferDTO> result = offerService.getOfferById(1L);

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

            Optional<OfferDTO> result = offerService.getOfferById(99L);

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
            OfferDTO newOfferDTO = new OfferDTO();
            newOfferDTO.setTitle("DevOps Engineer");
            newOfferDTO.setDescription("Infrastructure et CI/CD");
            newOfferDTO.setType("CDI");
            newOfferDTO.setAddress("Toulouse");
            newOfferDTO.setSalary(55000);
            newOfferDTO.setExperience("4-6 ans");

            Offer newOfferEntity = new Offer();
            newOfferEntity.setTitle("DevOps Engineer");
            newOfferEntity.setDescription("Infrastructure et CI/CD");
            newOfferEntity.setType("CDI");
            newOfferEntity.setAddress("Toulouse");
            newOfferEntity.setSalary(55000);
            newOfferEntity.setExperience("4-6 ans");

            OfferDTO resultDTO = new OfferDTO();
            resultDTO.setId(3L);
            resultDTO.setTitle("DevOps Engineer");
            resultDTO.setDescription("Infrastructure et CI/CD");
            resultDTO.setType("CDI");
            resultDTO.setAddress("Toulouse");
            resultDTO.setSalary(55000);
            resultDTO.setExperience("4-6 ans");
            resultDTO.setRecruiterId(1L);

            when(entityMapper.toOfferEntity(newOfferDTO)).thenReturn(newOfferEntity);
            when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));
            when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> {
                Offer saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toOfferDTO(any(Offer.class))).thenReturn(resultDTO);

            OfferDTO result = offerService.saveOffer(newOfferDTO, 1L);

            assertNotNull(result);
            assertEquals("DevOps Engineer", result.getTitle());
            assertEquals(1L, result.getRecruiterId());
            verify(entityMapper).toOfferEntity(newOfferDTO);
            verify(recruiterRepository).findById(1L);
            verify(offerRepository).save(any(Offer.class));
            verify(entityMapper).toOfferDTO(any(Offer.class));
        }

        @Test
        @DisplayName("Should save offer with null recruiter when not found")
        void shouldSaveOfferWithNullRecruiterWhenNotFound() {
            OfferDTO newOfferDTO = new OfferDTO();
            newOfferDTO.setTitle("DevOps Engineer");
            newOfferDTO.setDescription("Infrastructure");

            Offer newOfferEntity = new Offer();
            newOfferEntity.setTitle("DevOps Engineer");
            newOfferEntity.setDescription("Infrastructure");

            OfferDTO resultDTO = new OfferDTO();
            resultDTO.setId(3L);
            resultDTO.setTitle("DevOps Engineer");
            resultDTO.setDescription("Infrastructure");

            when(entityMapper.toOfferEntity(newOfferDTO)).thenReturn(newOfferEntity);
            when(recruiterRepository.findById(99L)).thenReturn(Optional.empty());
            when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> {
                Offer saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toOfferDTO(any(Offer.class))).thenReturn(resultDTO);

            OfferDTO result = offerService.saveOffer(newOfferDTO, 99L);

            assertNotNull(result);
            assertNull(result.getRecruiterId());
            verify(entityMapper).toOfferEntity(newOfferDTO);
            verify(recruiterRepository).findById(99L);
            verify(offerRepository).save(any(Offer.class));
            verify(entityMapper).toOfferDTO(any(Offer.class));
        }
    }

    @Nested
    @DisplayName("updateOffer Tests")
    class UpdateOfferTests {

        @Test
        @DisplayName("Should update offer when found")
        void shouldUpdateOfferWhenFound() {
            OfferDTO updatedData = new OfferDTO();
            updatedData.setTitle("Senior Engineer");
            updatedData.setDescription("Lead developer");
            updatedData.setType("CDI");
            updatedData.setAddress("Lyon");
            updatedData.setSalary(65000);
            updatedData.setExperience("7+ ans");
            updatedData.setPublicationDate(LocalDate.of(2026, 3, 1));
            updatedData.setExpirationDate(LocalDate.of(2026, 9, 1));

            OfferDTO resultDTO = new OfferDTO();
            resultDTO.setId(1L);
            resultDTO.setTitle("Senior Engineer");
            resultDTO.setDescription("Lead developer");
            resultDTO.setType("CDI");
            resultDTO.setAddress("Lyon");
            resultDTO.setSalary(65000);
            resultDTO.setExperience("7+ ans");
            resultDTO.setPublicationDate(LocalDate.of(2026, 3, 1));
            resultDTO.setExpirationDate(LocalDate.of(2026, 9, 1));
            resultDTO.setRecruiterId(1L);

            when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));
            when(offerRepository.saveAndFlush(offer1)).thenReturn(offer1);
            when(entityMapper.toOfferDTO(offer1)).thenReturn(resultDTO);

            OfferDTO result = offerService.updateOffer(1L, updatedData);

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
            verify(entityMapper).toOfferDTO(offer1);
        }

        @Test
        @DisplayName("Should throw RuntimeException when offer not found")
        void shouldThrowWhenOfferNotFound() {
            OfferDTO updatedData = new OfferDTO();
            updatedData.setTitle("Senior Engineer");

            when(offerRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
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
