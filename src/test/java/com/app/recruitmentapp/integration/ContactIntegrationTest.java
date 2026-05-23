package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Contact API - Integration Tests")
class ContactIntegrationTest extends AbstractIntegrationTest {

    private String token;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = createCandidate("contact_candidate@test.com");
        token = generateToken("contact_candidate@test.com", "CANDIDATE");
    }

    @Test
    @DisplayName("POST /contact/{idUserSend} should send a contact message")
    void sendMessageByUser_shouldReturn201() {
        Contact contact = new Contact();
        contact.setName("John Doe");
        contact.setEmail("john@test.com");
        contact.setSubject("Question");
        contact.setMessage("Hello, I have a question");

        ResponseEntity<Contact> response = restTemplate.exchange(
                "/contact/" + candidate.getId(),
                HttpMethod.POST,
                authEntity(contact, token),
                Contact.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSubject()).isEqualTo("Question");
    }
}
