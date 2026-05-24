package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.OfferDTO;
import com.app.recruitmentapp.services.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OfferController Unit Tests")
class OfferControllerTest {

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OfferController offerController;

    private OfferDTO testOfferDTO;

    @BeforeEach
    void setUp() {
        testOfferDTO = new OfferDTO();
        testOfferDTO.setId(1L);
        testOfferDTO.setTitle("Software Engineer");
        testOfferDTO.setDescription("Great opportunity");
    }

    @Nested
    @DisplayName("GET /offer/offers")
    class GetAllOffersTests {

        @Test
        @DisplayName("Should return all offers")
        void shouldReturnAllOffers() {
            when(offerService.getAllOffers()).thenReturn(List.of(testOfferDTO));

            List<OfferDTO> result = offerController.getAllOffers();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /offer/{id}")
    class GetOfferByIdTests {

        @Test
        @DisplayName("Should return offer when found")
        void shouldReturnOfferWhenFound() {
            when(offerService.getOfferById(1L)).thenReturn(Optional.of(testOfferDTO));

            ResponseEntity<OfferDTO> response = offerController.getOfferById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(offerService.getOfferById(99L)).thenReturn(Optional.empty());

            ResponseEntity<OfferDTO> response = offerController.getOfferById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /offer/{idRecruiter}")
    class AddOfferTests {

        @Test
        @DisplayName("Should add offer successfully")
        void shouldAddOfferSuccessfully() {
            when(offerService.saveOffer(testOfferDTO, 1L)).thenReturn(testOfferDTO);

            ResponseEntity<OfferDTO> response = offerController.addOffer(testOfferDTO, 1L);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /offer/{id}")
    class UpdateOfferTests {

        @Test
        @DisplayName("Should update offer successfully")
        void shouldUpdateOfferSuccessfully() {
            when(offerService.updateOffer(1L, testOfferDTO)).thenReturn(testOfferDTO);

            ResponseEntity<OfferDTO> response = offerController.updateOffer(1L, testOfferDTO);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when update fails")
        void shouldReturn404WhenUpdateFails() {
            when(offerService.updateOffer(99L, testOfferDTO)).thenThrow(new RuntimeException("Offer not found"));

            ResponseEntity<OfferDTO> response = offerController.updateOffer(99L, testOfferDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /offer/{id}")
    class DeleteOfferTests {

        @Test
        @DisplayName("Should delete offer successfully")
        void shouldDeleteOfferSuccessfully() {
            doNothing().when(offerService).deleteOffer(1L);

            ResponseEntity<String> response = offerController.deleteOffer(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Offer deleted successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Offer not found")).when(offerService).deleteOffer(99L);

            ResponseEntity<String> response = offerController.deleteOffer(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /offer/files/{filename}")
    class GetFileTests {

        @Test
        @DisplayName("Should return file resource")
        void shouldReturnFileResource() {
            Resource mockResource = mock(Resource.class);
            when(offerService.getFile("test.pdf")).thenReturn(ResponseEntity.ok(mockResource));

            ResponseEntity<Resource> response = offerController.getFile("test.pdf");

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }
}
