package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.CandidacyDTO;
import com.app.recruitmentapp.entities.Candidacy;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Status;
import com.app.recruitmentapp.mapper.EntityMapper;
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
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private CandidacyServiceImpl candidacyService;

    private Candidacy candidacy1;
    private Candidacy candidacy2;
    private Candidate candidate;
    private Offer offer;
    private CandidacyDTO candidacy1DTO;
    private CandidacyDTO candidacy2DTO;

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

        candidacy1DTO = new CandidacyDTO();
        candidacy1DTO.setId(1L);
        candidacy1DTO.setSubmissionDate(candidacy1.getSubmissionDate());
        candidacy1DTO.setStatus("PENDING");
        candidacy1DTO.setScore(85.0);
        candidacy1DTO.setCandidateId(1L);
        candidacy1DTO.setOfferId(1L);

        candidacy2DTO = new CandidacyDTO();
        candidacy2DTO.setId(2L);
        candidacy2DTO.setSubmissionDate(candidacy2.getSubmissionDate());
        candidacy2DTO.setStatus("ACCEPTED");
        candidacy2DTO.setScore(92.0);
        candidacy2DTO.setCandidateId(1L);
        candidacy2DTO.setOfferId(1L);
    }

    @Nested
    @DisplayName("getAllCandidacies Tests")
    class GetAllCandidaciesTests {

        @Test
        @DisplayName("Should return all candidacies")
        void shouldGetAllCandidaciesSuccessfully() {
            List<Candidacy> mockCandidacies = List.of(candidacy1, candidacy2);
            when(candidacyRepository.findAll()).thenReturn(mockCandidacies);
            when(entityMapper.toCandidacyDTOList(mockCandidacies)).thenReturn(List.of(candidacy1DTO, candidacy2DTO));

            List<CandidacyDTO> result = candidacyService.getAllCandidacies();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("PENDING", result.get(0).getStatus());
            assertEquals("ACCEPTED", result.get(1).getStatus());
            verify(candidacyRepository).findAll();
            verify(entityMapper).toCandidacyDTOList(mockCandidacies);
        }
    }

    @Nested
    @DisplayName("getCandidacyById Tests")
    class GetCandidacyByIdTests {

        @Test
        @DisplayName("Should return candidacy when found")
        void shouldReturnCandidacyWhenFound() {
            when(candidacyRepository.findById(1L)).thenReturn(Optional.of(candidacy1));
            when(entityMapper.toCandidacyDTO(candidacy1)).thenReturn(candidacy1DTO);

            Optional<CandidacyDTO> result = candidacyService.getCandidacyById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals(85.0, result.get().getScore());
            verify(candidacyRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(candidacyRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<CandidacyDTO> result = candidacyService.getCandidacyById(99L);

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
            CandidacyDTO inputDTO = new CandidacyDTO();
            inputDTO.setSubmissionDate(candidacy1.getSubmissionDate());
            inputDTO.setStatus(candidacy1DTO.getStatus());
            inputDTO.setScore(85.0);

            Candidacy inputEntity = new Candidacy();
            inputEntity.setSubmissionDate(inputDTO.getSubmissionDate());
            inputEntity.setStatus(Status.PENDING);
            inputEntity.setScore(inputDTO.getScore());

            CandidacyDTO resultDTO = new CandidacyDTO();
            resultDTO.setId(1L);
            resultDTO.setSubmissionDate(inputDTO.getSubmissionDate());
            resultDTO.setStatus("PENDING");
            resultDTO.setScore(85.0);

            when(entityMapper.toCandidacyEntity(inputDTO)).thenReturn(inputEntity);
            when(candidacyRepository.save(inputEntity)).thenReturn(inputEntity);
            when(entityMapper.toCandidacyDTO(inputEntity)).thenReturn(resultDTO);

            CandidacyDTO result = candidacyService.saveCandidacy(inputDTO);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("PENDING", result.getStatus());
            verify(entityMapper).toCandidacyEntity(inputDTO);
            verify(candidacyRepository).save(inputEntity);
            verify(entityMapper).toCandidacyDTO(inputEntity);
        }
    }

    @Nested
    @DisplayName("updateCandidacy Tests")
    class UpdateCandidacyTests {

        @Test
        @DisplayName("Should update candidacy when found")
        void shouldUpdateCandidacyWhenFound() {
            CandidacyDTO updatedData = new CandidacyDTO();
            updatedData.setSubmissionDate(new Date());
            updatedData.setStatus("ACCEPTED");
            updatedData.setScore(95.0);

            CandidacyDTO resultDTO = new CandidacyDTO();
            resultDTO.setId(1L);
            resultDTO.setSubmissionDate(updatedData.getSubmissionDate());
            resultDTO.setStatus("ACCEPTED");
            resultDTO.setScore(95.0);

            when(candidacyRepository.findById(1L)).thenReturn(Optional.of(candidacy1));
            when(candidacyRepository.save(candidacy1)).thenReturn(candidacy1);
            when(entityMapper.toCandidacyDTO(candidacy1)).thenReturn(resultDTO);

            CandidacyDTO result = candidacyService.updateCandidacy(1L, updatedData);

            assertNotNull(result);
            assertEquals("ACCEPTED", result.getStatus());
            assertEquals(95.0, result.getScore());
            verify(candidacyRepository).findById(1L);
            verify(candidacyRepository).save(candidacy1);
            verify(entityMapper).toCandidacyDTO(candidacy1);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when candidacy not found")
        void shouldThrowWhenCandidacyNotFound() {
            when(candidacyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> candidacyService.updateCandidacy(99L, candidacy1DTO));
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
