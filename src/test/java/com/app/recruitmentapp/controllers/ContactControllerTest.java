package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.ContactDTO;
import com.app.recruitmentapp.services.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContactController Unit Tests")
class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @Nested
    @DisplayName("POST /contact/{idUserSend}")
    class SendMessageByUserTests {

        @Test
        @DisplayName("Should send contact message successfully")
        void shouldSendMessageSuccessfully() {
            ContactDTO contactDTO = new ContactDTO();
            ContactDTO savedDTO = new ContactDTO();
            when(contactService.sendMessageContact(contactDTO, 1L)).thenReturn(savedDTO);

            ResponseEntity<ContactDTO> response = contactController.sendMessageByUser(contactDTO, 1L);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }
}
