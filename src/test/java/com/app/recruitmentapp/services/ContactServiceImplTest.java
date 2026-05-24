package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.ContactDTO;
import com.app.recruitmentapp.entities.Contact;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.repositories.ContactRepository;
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
@DisplayName("ContactServiceImpl Unit Tests")
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EntityMapper entityMapper;
    @InjectMocks
    private ContactServiceImpl contactService;

    private Contact contact1;
    private Contact contact2;
    private User user;
    private ContactDTO contact1DTO;
    private ContactDTO contact2DTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setName("Test");
        user.setFirstName("User");

        contact1 = new Contact();
        contact1.setId(1L);
        contact1.setName("John Doe");
        contact1.setSubject("Question");
        contact1.setEmail("john@test.com");
        contact1.setPhone("123456789");
        contact1.setMessage("Hello, I have a question.");
        contact1.setUserSend(user);

        contact2 = new Contact();
        contact2.setId(2L);
        contact2.setName("Jane Doe");
        contact2.setSubject("Support");
        contact2.setEmail("jane@test.com");
        contact2.setPhone("987654321");
        contact2.setMessage("I need support.");
        contact2.setUserSend(null);

        contact1DTO = new ContactDTO();
        contact1DTO.setId(1L);
        contact1DTO.setName("John Doe");
        contact1DTO.setSubject("Question");
        contact1DTO.setEmail("john@test.com");
        contact1DTO.setPhone("123456789");
        contact1DTO.setMessage("Hello, I have a question.");
        contact1DTO.setUserSendId(1L);

        contact2DTO = new ContactDTO();
        contact2DTO.setId(2L);
        contact2DTO.setName("Jane Doe");
        contact2DTO.setSubject("Support");
        contact2DTO.setEmail("jane@test.com");
        contact2DTO.setPhone("987654321");
        contact2DTO.setMessage("I need support.");
        contact2DTO.setUserSendId(null);
    }

    @Nested
    @DisplayName("getAllMessagesContact Tests")
    class GetAllMessagesContactTests {

        @Test
        @DisplayName("Should return all contact messages")
        void shouldGetAllMessagesContactSuccessfully() {
            List<Contact> mockContacts = List.of(contact1, contact2);
            when(contactRepository.findAll()).thenReturn(mockContacts);
            when(entityMapper.toContactDTOList(mockContacts)).thenReturn(List.of(contact1DTO, contact2DTO));

            List<ContactDTO> result = contactService.getAllMessagesContact();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("john@test.com", result.get(0).getEmail());
            assertEquals("jane@test.com", result.get(1).getEmail());
            verify(contactRepository).findAll();
            verify(entityMapper).toContactDTOList(mockContacts);
        }
    }

    @Nested
    @DisplayName("sendMessageContact Tests")
    class SendMessageContactTests {

        @Test
        @DisplayName("Should send message when user is found")
        void shouldSendMessageWhenUserFound() {
            ContactDTO newContactDTO = new ContactDTO();
            newContactDTO.setName("New User");
            newContactDTO.setSubject("Inquiry");
            newContactDTO.setEmail("new@test.com");
            newContactDTO.setMessage("New inquiry message.");

            Contact newContactEntity = new Contact();
            newContactEntity.setName("New User");
            newContactEntity.setSubject("Inquiry");
            newContactEntity.setEmail("new@test.com");
            newContactEntity.setMessage("New inquiry message.");

            ContactDTO resultDTO = new ContactDTO();
            resultDTO.setId(3L);
            resultDTO.setName("New User");
            resultDTO.setSubject("Inquiry");
            resultDTO.setEmail("new@test.com");
            resultDTO.setMessage("New inquiry message.");
            resultDTO.setUserSendId(1L);

            when(entityMapper.toContactEntity(newContactDTO)).thenReturn(newContactEntity);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> {
                Contact saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });
            when(entityMapper.toContactDTO(any(Contact.class))).thenReturn(resultDTO);

            ContactDTO result = contactService.sendMessageContact(newContactDTO, 1L);

            assertNotNull(result);
            assertEquals(3L, result.getId());
            assertEquals("New User", result.getName());
            assertEquals("Inquiry", result.getSubject());
            assertEquals(1L, result.getUserSendId());
            verify(entityMapper).toContactEntity(newContactDTO);
            verify(userRepository).findById(1L);
            verify(contactRepository).save(any(Contact.class));
            verify(entityMapper).toContactDTO(any(Contact.class));
        }

        @Test
        @DisplayName("Should send message with null userSend when user not found")
        void shouldSendMessageWithNullUserWhenUserNotFound() {
            ContactDTO newContactDTO = new ContactDTO();
            newContactDTO.setName("Anonymous");
            newContactDTO.setSubject("Feedback");
            newContactDTO.setEmail("anon@test.com");
            newContactDTO.setMessage("Anonymous feedback.");

            Contact newContactEntity = new Contact();
            newContactEntity.setName("Anonymous");
            newContactEntity.setSubject("Feedback");
            newContactEntity.setEmail("anon@test.com");
            newContactEntity.setMessage("Anonymous feedback.");

            ContactDTO resultDTO = new ContactDTO();
            resultDTO.setId(4L);
            resultDTO.setName("Anonymous");
            resultDTO.setSubject("Feedback");
            resultDTO.setEmail("anon@test.com");
            resultDTO.setMessage("Anonymous feedback.");

            when(entityMapper.toContactEntity(newContactDTO)).thenReturn(newContactEntity);
            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> {
                Contact saved = invocation.getArgument(0);
                saved.setId(4L);
                return saved;
            });
            when(entityMapper.toContactDTO(any(Contact.class))).thenReturn(resultDTO);

            ContactDTO result = contactService.sendMessageContact(newContactDTO, 99L);

            assertNotNull(result);
            assertEquals(4L, result.getId());
            assertNull(result.getUserSendId());
            verify(entityMapper).toContactEntity(newContactDTO);
            verify(userRepository).findById(99L);
            verify(contactRepository).save(any(Contact.class));
            verify(entityMapper).toContactDTO(any(Contact.class));
        }
    }
}
