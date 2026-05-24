package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.MessageDTO;
import com.app.recruitmentapp.entities.Message;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.mapper.EntityMapper;
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
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private MessageServiceImpl messageService;

    private User userSend;
    private User userReceive;
    private Message message1;
    private Message message2;
    private MessageDTO message1DTO;
    private MessageDTO message2DTO;

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

        message1DTO = new MessageDTO();
        message1DTO.setId(1L);
        message1DTO.setFullName("John Doe");
        message1DTO.setSubject("Demande d'information");
        message1DTO.setMessage("Bonjour, j'ai une question.");
        message1DTO.setUserSendId(1L);
        message1DTO.setUserReceiveId(2L);

        message2DTO = new MessageDTO();
        message2DTO.setId(2L);
        message2DTO.setFullName("Jane Smith");
        message2DTO.setSubject("Candidature");
        message2DTO.setMessage("Je postule pour le poste.");
        message2DTO.setUserSendId(2L);
        message2DTO.setUserReceiveId(1L);
    }

    @Nested
    @DisplayName("getAllMessages Tests")
    class GetAllMessagesTests {

        @Test
        @DisplayName("Should return all messages")
        void shouldGetAllMessagesSuccessfully() {
            List<Message> mockMessages = List.of(message1, message2);
            when(messageRepository.findAll()).thenReturn(mockMessages);
            when(entityMapper.toMessageDTOList(mockMessages)).thenReturn(List.of(message1DTO, message2DTO));

            List<MessageDTO> result = messageService.getAllMessages();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Demande d'information", result.get(0).getSubject());
            assertEquals("Candidature", result.get(1).getSubject());
            verify(messageRepository).findAll();
            verify(entityMapper).toMessageDTOList(mockMessages);
        }
    }

    @Nested
    @DisplayName("getMessageById Tests")
    class GetMessageByIdTests {

        @Test
        @DisplayName("Should return message when found")
        void shouldReturnMessageWhenFound() {
            when(messageRepository.findById(1L)).thenReturn(Optional.of(message1));
            when(entityMapper.toMessageDTO(message1)).thenReturn(message1DTO);

            Optional<MessageDTO> result = messageService.getMessageById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("John Doe", result.get().getFullName());
            verify(messageRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(messageRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<MessageDTO> result = messageService.getMessageById(99L);

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
            MessageDTO newMessageDTO = new MessageDTO();
            newMessageDTO.setFullName("Alice");
            newMessageDTO.setSubject("Offre d'emploi");
            newMessageDTO.setMessage("Je suis interesse par votre offre.");

            Message newMessageEntity = new Message();
            newMessageEntity.setFullName("Alice");
            newMessageEntity.setSubject("Offre d'emploi");
            newMessageEntity.setMessage("Je suis interesse par votre offre.");

            MessageDTO resultDTO = new MessageDTO();
            resultDTO.setId(3L);
            resultDTO.setFullName("Alice");
            resultDTO.setSubject("Offre d'emploi");
            resultDTO.setMessage("Je suis interesse par votre offre.");
            resultDTO.setUserSendId(1L);
            resultDTO.setUserReceiveId(2L);

            when(entityMapper.toMessageEntity(newMessageDTO)).thenReturn(newMessageEntity);
            when(userRepository.findById(1L)).thenReturn(Optional.of(userSend));
            when(userRepository.findById(2L)).thenReturn(Optional.of(userReceive));
            when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
                Message saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toMessageDTO(any(Message.class))).thenReturn(resultDTO);

            MessageDTO result = messageService.sendMessage(newMessageDTO, 1L, 2L);

            assertNotNull(result);
            assertEquals("Alice", result.getFullName());
            assertEquals("Offre d'emploi", result.getSubject());
            assertEquals(1L, result.getUserSendId());
            assertEquals(2L, result.getUserReceiveId());
            verify(entityMapper).toMessageEntity(newMessageDTO);
            verify(userRepository).findById(1L);
            verify(userRepository).findById(2L);
            verify(messageRepository).save(any(Message.class));
            verify(entityMapper).toMessageDTO(any(Message.class));
        }

        @Test
        @DisplayName("Should send message with null users when not found")
        void shouldSendMessageWithNullUsersWhenNotFound() {
            MessageDTO newMessageDTO = new MessageDTO();
            newMessageDTO.setFullName("Alice");
            newMessageDTO.setSubject("Offre d'emploi");
            newMessageDTO.setMessage("Message test.");

            Message newMessageEntity = new Message();
            newMessageEntity.setFullName("Alice");
            newMessageEntity.setSubject("Offre d'emploi");
            newMessageEntity.setMessage("Message test.");

            MessageDTO resultDTO = new MessageDTO();
            resultDTO.setId(3L);
            resultDTO.setFullName("Alice");
            resultDTO.setSubject("Offre d'emploi");
            resultDTO.setMessage("Message test.");

            when(entityMapper.toMessageEntity(newMessageDTO)).thenReturn(newMessageEntity);
            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            when(userRepository.findById(98L)).thenReturn(Optional.empty());
            when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
                Message saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toMessageDTO(any(Message.class))).thenReturn(resultDTO);

            MessageDTO result = messageService.sendMessage(newMessageDTO, 99L, 98L);

            assertNotNull(result);
            assertNull(result.getUserSendId());
            assertNull(result.getUserReceiveId());
            verify(entityMapper).toMessageEntity(newMessageDTO);
            verify(userRepository).findById(99L);
            verify(userRepository).findById(98L);
            verify(messageRepository).save(any(Message.class));
            verify(entityMapper).toMessageDTO(any(Message.class));
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
