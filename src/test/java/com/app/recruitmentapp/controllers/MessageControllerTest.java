package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.MessageDTO;
import com.app.recruitmentapp.services.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageController Unit Tests")
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    private MessageDTO testMessageDTO;

    @BeforeEach
    void setUp() {
        testMessageDTO = new MessageDTO();
        testMessageDTO.setId(1L);
        testMessageDTO.setMessage("Hello");
    }

    @Nested
    @DisplayName("GET /message/messages")
    class GetAllMessagesTests {

        @Test
        @DisplayName("Should return all messages")
        void shouldReturnAllMessages() {
            when(messageService.getAllMessages()).thenReturn(List.of(testMessageDTO));

            List<MessageDTO> result = messageController.getAllMessages();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("GET /message/{id}")
    class GetMessageByIdTests {

        @Test
        @DisplayName("Should return message when found")
        void shouldReturnMessageWhenFound() {
            when(messageService.getMessageById(1L)).thenReturn(Optional.of(testMessageDTO));

            ResponseEntity<MessageDTO> response = messageController.getMessageById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() {
            when(messageService.getMessageById(99L)).thenReturn(Optional.empty());

            ResponseEntity<MessageDTO> response = messageController.getMessageById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /message/{idUserSend}/{idUserReceive}")
    class SendMessageByUserTests {

        @Test
        @DisplayName("Should send message successfully")
        void shouldSendMessageSuccessfully() {
            MessageDTO savedDTO = new MessageDTO();
            savedDTO.setId(1L);
            when(messageService.sendMessage(testMessageDTO, 1L, 2L)).thenReturn(savedDTO);

            ResponseEntity<MessageDTO> response = messageController.sendMessageByUser(testMessageDTO, 1L, 2L);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /message/{id}")
    class DeleteMessageTests {

        @Test
        @DisplayName("Should delete message successfully")
        void shouldDeleteMessageSuccessfully() {
            doNothing().when(messageService).deleteMessage(1L);

            ResponseEntity<String> response = messageController.deleteMessage(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Message deleted successfully", response.getBody());
        }

        @Test
        @DisplayName("Should return 404 when delete fails")
        void shouldReturn404WhenDeleteFails() {
            doThrow(new RuntimeException("Message not found")).when(messageService).deleteMessage(99L);

            ResponseEntity<String> response = messageController.deleteMessage(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
