package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidacy;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Status;
import com.app.recruitmentapp.repositories.CandidacyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CandidacyServiceImpl Unit Tests")
class CandidacyServiceImplTest {

    @Mock
    private CandidacyRepository candidacyRepository;

    @InjectMocks
    private CandidacyServiceImpl candidacyService;

    private Candidacy candidacy1;
    private Candidacy candidacy2;
    private Candidate candidate;
    private Offer offer;

    @BeforeEach
    void setUp() {
        candidate = new Candidate();
        candidate.setId(1L);

        offer = new Offer();
        offer.setId(1L);

        candidacy1 = new Candidacy();
        candidacy1.setId(1L);
        candidacy1.setSubmissionDate(new Date());
        candidacy1.setStatus(Status.PENDING);
        candidacy1.setScore(85.0);
        candidacy1.setCandidate(candidate);
        candidacy1.setOffer(offer);

        candidacy2 = new Candidacy();
        candidacy2.setId(2L);
        candidacy2.setSubmissionDate(new Date());
        candidacy2.setStatus(Status.ACCEPTED);
        candidacy2.setScore(92.0);
        candidacy2.setCandidate(candidate);
        candidacy2.setOffer(offer);
    }

    @Nested
    @DisplayName("getAllCandidacies Tests")
    class GetAllCandidaciesTests {

        @Test
        @DisplayName("Should return all candidacies")
        void shouldGetAllCandidaciesSuccessfully() {
            List<Candidacy> mockCandidacies = List.of(candidacy1, candidacy2);
            when(candidacyRepository.findAll()).thenReturn(mockCandidacies);

            List<Candidacy> result = candidacyService.getAllCandidacies();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(Status.PENDING, result.get(0).getStatus());
            assertEquals(Status.ACCEPTED, result.get(1).getStatus());
            verify(candidacyRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getCandidacyById Tests")
    class GetCandidacyByIdTests {

        @Test
        @DisplayName("Should return candidacy when found")
        void shouldReturnCandidacyWhenFound() {
            when(candidacyRepository.findById(1L)).thenReturn(Optional.of(candidacy1));

            Optional<Candidacy> result = candidacyService.getCandidacyById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals(85.0, result.get().getScore());
            verify(candidacyRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(candidacyRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Candidacy> result = candidacyService.getCandidacyById(99L);

            assertFalse(result.isPresent());
            verify(candidacyRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("saveCandidacy Tests")
    class SaveCandidacyTests {

        @Test
        @DisplayName("Should save candidacy successfully")
        void shouldSaveCandidacySuccessfully() {
            when(candidacyRepository.save(candidacy1)).thenReturn(candidacy1);

            Candidacy result = candidacyService.saveCandidacy(candidacy1);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(Status.PENDING, result.getStatus());
            verify(candidacyRepository).save(candidacy1);
        }
    }

    @Nested
    @DisplayName("updateCandidacy Tests")
    class UpdateCandidacyTests {

        @Test
        @DisplayName("Should update candidacy when found")
        void shouldUpdateCandidacyWhenFound() {
            Candidacy updatedCandidacy = new Candidacy();
            updatedCandidacy.setSubmissionDate(new Date());
            updatedCandidacy.setStatus(Status.ACCEPTED);
            updatedCandidacy.setScore(95.0);
            updatedCandidacy.setCandidate(candidate);
            updatedCandidacy.setOffer(offer);

            when(candidacyRepository.findById(1L)).thenReturn(Optional.of(candidacy1));
            when(candidacyRepository.save(candidacy1)).thenReturn(candidacy1);

            Candidacy result = candidacyService.updateCandidacy(1L, updatedCandidacy);

            assertNotNull(result);
            assertEquals(Status.ACCEPTED, result.getStatus());
            assertEquals(95.0, result.getScore());
            verify(candidacyRepository).findById(1L);
            verify(candidacyRepository).save(candidacy1);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when candidacy not found")
        void shouldThrowWhenCandidacyNotFound() {
            when(candidacyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> candidacyService.updateCandidacy(99L, candidacy1));
            verify(candidacyRepository).findById(99L);
            verify(candidacyRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deleteCandidacy Tests")
    class DeleteCandidacyTests {

        @Test
        @DisplayName("Should delete candidacy when found")
        void shouldDeleteCandidacyWhenFound() {
            when(candidacyRepository.findById(1L)).thenReturn(Optional.of(candidacy1));

            candidacyService.deleteCandidacy(1L);

            verify(candidacyRepository).findById(1L);
            verify(candidacyRepository).delete(candidacy1);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when candidacy not found")
        void shouldThrowWhenCandidacyNotFound() {
            when(candidacyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> candidacyService.deleteCandidacy(99L));
            verify(candidacyRepository).findById(99L);
            verify(candidacyRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("acceptApplication Tests")
    class AcceptApplicationTests {

        @Test
        @DisplayName("Should accept candidacy successfully")
        void shouldAcceptCandidacySuccessfully() {
            when(candidacyRepository.findById(1L)).thenReturn(Optional.of(candidacy1));
            when(candidacyRepository.save(candidacy1)).thenReturn(candidacy1);

            candidacyService.acceptApplication(1L);

            assertEquals(Status.ACCEPTED, candidacy1.getStatus());
            verify(candidacyRepository).findById(1L);
            verify(candidacyRepository).save(candidacy1);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when candidacy not found")
        void shouldThrowWhenCandidacyNotFound() {
            when(candidacyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> candidacyService.acceptApplication(99L));
            verify(candidacyRepository).findById(99L);
            verify(candidacyRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("declineApplication Tests")
    class DeclineApplicationTests {

        @Test
        @DisplayName("Should decline candidacy successfully")
        void shouldDeclineCandidacySuccessfully() {
            when(candidacyRepository.findById(1L)).thenReturn(Optional.of(candidacy1));
            when(candidacyRepository.save(candidacy1)).thenReturn(candidacy1);

            candidacyService.declineApplication(1L);

            assertEquals(Status.DECLINED, candidacy1.getStatus());
            verify(candidacyRepository).findById(1L);
            verify(candidacyRepository).save(candidacy1);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when candidacy not found")
        void shouldThrowWhenCandidacyNotFound() {
            when(candidacyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> candidacyService.declineApplication(99L));
            verify(candidacyRepository).findById(99L);
            verify(candidacyRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("candidacyExists Tests")
    class CandidacyExistsTests {

        @Test
        @DisplayName("Should return true when candidacy exists")
        void shouldReturnTrueWhenExists() {
            when(candidacyRepository.existsByOfferIdAndCandidateId(1L, 1L)).thenReturn(true);

            boolean result = candidacyService.candidacyExists(1L, 1L);

            assertTrue(result);
            verify(candidacyRepository).existsByOfferIdAndCandidateId(1L, 1L);
        }

        @Test
        @DisplayName("Should return false when candidacy does not exist")
        void shouldReturnFalseWhenNotExists() {
            when(candidacyRepository.existsByOfferIdAndCandidateId(1L, 99L)).thenReturn(false);

            boolean result = candidacyService.candidacyExists(1L, 99L);

            assertFalse(result);
            verify(candidacyRepository).existsByOfferIdAndCandidateId(1L, 99L);
        }
    }

}
