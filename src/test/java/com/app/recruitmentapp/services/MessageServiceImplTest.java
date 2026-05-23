package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Message;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.MessageRepository;
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
@DisplayName("MessageServiceImpl Unit Tests")
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MessageServiceImpl messageService;

    private User userSend;
    private User userReceive;
    private Message message1;
    private Message message2;

    @BeforeEach
    void setUp() {
        userSend = new User();
        userSend.setId(1L);
        userSend.setEmail("sender@test.com");

        userReceive = new User();
        userReceive.setId(2L);
        userReceive.setEmail("receiver@test.com");

        message1 = new Message();
        message1.setId(1L);
        message1.setFullName("John Doe");
        message1.setSubject("Demande d'information");
        message1.setMessage("Bonjour, j'ai une question.");
        message1.setUserSend(userSend);
        message1.setUserReceive(userReceive);

        message2 = new Message();
        message2.setId(2L);
        message2.setFullName("Jane Smith");
        message2.setSubject("Candidature");
        message2.setMessage("Je postule pour le poste.");
        message2.setUserSend(userReceive);
        message2.setUserReceive(userSend);
    }

    @Nested
    @DisplayName("getAllMessages Tests")
    class GetAllMessagesTests {

        @Test
        @DisplayName("Should return all messages")
        void shouldGetAllMessagesSuccessfully() {
            List<Message> mockMessages = List.of(message1, message2);
            when(messageRepository.findAll()).thenReturn(mockMessages);

            List<Message> result = messageService.getAllMessages();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Demande d'information", result.get(0).getSubject());
            assertEquals("Candidature", result.get(1).getSubject());
            verify(messageRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getMessageById Tests")
    class GetMessageByIdTests {

        @Test
        @DisplayName("Should return message when found")
        void shouldReturnMessageWhenFound() {
            when(messageRepository.findById(1L)).thenReturn(Optional.of(message1));

            Optional<Message> result = messageService.getMessageById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("John Doe", result.get().getFullName());
            verify(messageRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(messageRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Message> result = messageService.getMessageById(99L);

            assertFalse(result.isPresent());
            verify(messageRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("sendMessage Tests")
    class SendMessageTests {

        @Test
        @DisplayName("Should send message successfully")
        void shouldSendMessageSuccessfully() {
            Message newMessage = new Message();
            newMessage.setFullName("Alice");
            newMessage.setSubject("Offre d'emploi");
            newMessage.setMessage("Je suis intéressé par votre offre.");

            when(userRepository.findById(1L)).thenReturn(Optional.of(userSend));
            when(userRepository.findById(2L)).thenReturn(Optional.of(userReceive));
            when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
                Message saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Message result = messageService.sendMessage(newMessage, 1L, 2L);

            assertNotNull(result);
            assertEquals("Alice", result.getFullName());
            assertEquals("Offre d'emploi", result.getSubject());
            assertEquals(userSend, result.getUserSend());
            assertEquals(userReceive, result.getUserReceive());
            verify(userRepository).findById(1L);
            verify(userRepository).findById(2L);
            verify(messageRepository).save(any(Message.class));
        }

        @Test
        @DisplayName("Should send message with null users when not found")
        void shouldSendMessageWithNullUsersWhenNotFound() {
            Message newMessage = new Message();
            newMessage.setFullName("Alice");
            newMessage.setSubject("Offre d'emploi");
            newMessage.setMessage("Message test.");

            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            when(userRepository.findById(98L)).thenReturn(Optional.empty());
            when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
                Message saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Message result = messageService.sendMessage(newMessage, 99L, 98L);

            assertNotNull(result);
            assertNull(result.getUserSend());
            assertNull(result.getUserReceive());
            verify(userRepository).findById(99L);
            verify(userRepository).findById(98L);
            verify(messageRepository).save(any(Message.class));
        }
    }

    @Nested
    @DisplayName("deleteMessage Tests")
    class DeleteMessageTests {

        @Test
        @DisplayName("Should delete message when found")
        void shouldDeleteMessageWhenFound() {
            when(messageRepository.existsById(1L)).thenReturn(true);

            messageService.deleteMessage(1L);

            verify(messageRepository).existsById(1L);
            verify(messageRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw RuntimeException when not found")
        void shouldThrowWhenMessageNotFound() {
            when(messageRepository.existsById(99L)).thenReturn(false);

            assertThrows(RuntimeException.class,
                    () -> messageService.deleteMessage(99L));
            verify(messageRepository).existsById(99L);
            verify(messageRepository, never()).deleteById(anyLong());
        }
    }
}
