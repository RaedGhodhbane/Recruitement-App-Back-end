package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Contact;
import com.app.recruitmentapp.entities.User;
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
    @InjectMocks
    private ContactServiceImpl contactService;

    private Contact contact1;
    private Contact contact2;
    private User user;

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
    }

    @Nested
    @DisplayName("getAllMessagesContact Tests")
    class GetAllMessagesContactTests {

        @Test
        @DisplayName("Should return all contact messages")
        void shouldGetAllMessagesContactSuccessfully() {
            List<Contact> mockContacts = List.of(contact1, contact2);
            when(contactRepository.findAll()).thenReturn(mockContacts);

            List<Contact> result = contactService.getAllMessagesContact();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("john@test.com", result.get(0).getEmail());
            assertEquals("jane@test.com", result.get(1).getEmail());
            verify(contactRepository).findAll();
        }
    }

    @Nested
    @DisplayName("sendMessageContact Tests")
    class SendMessageContactTests {

        @Test
        @DisplayName("Should send message when user is found")
        void shouldSendMessageWhenUserFound() {
            Contact newContact = new Contact();
            newContact.setName("New User");
            newContact.setSubject("Inquiry");
            newContact.setEmail("new@test.com");
            newContact.setMessage("New inquiry message.");

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> {
                Contact saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            Contact result = contactService.sendMessageContact(newContact, 1L);

            assertNotNull(result);
            assertEquals(3L, result.getId());
            assertEquals("New User", result.getName());
            assertEquals("Inquiry", result.getSubject());
            assertEquals(user, result.getUserSend());
            verify(userRepository).findById(1L);
            verify(contactRepository).save(any(Contact.class));
        }

        @Test
        @DisplayName("Should send message with null userSend when user not found")
        void shouldSendMessageWithNullUserWhenUserNotFound() {
            Contact newContact = new Contact();
            newContact.setName("Anonymous");
            newContact.setSubject("Feedback");
            newContact.setEmail("anon@test.com");
            newContact.setMessage("Anonymous feedback.");

            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> {
                Contact saved = invocation.getArgument(0);
                saved.setId(4L);
                return saved;
            });

            Contact result = contactService.sendMessageContact(newContact, 99L);

            assertNotNull(result);
            assertEquals(4L, result.getId());
            assertNull(result.getUserSend());
            verify(userRepository).findById(99L);
            verify(contactRepository).save(any(Contact.class));
        }
    }
}
