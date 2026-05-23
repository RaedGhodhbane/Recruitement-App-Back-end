package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Message;
import com.app.recruitmentapp.entities.Recruiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Message API - Integration Tests")
class MessageIntegrationTest extends AbstractIntegrationTest {

    private Candidate sender;
    private Recruiter receiver;
    private String senderToken;

    @BeforeEach
    void setUp() {
        sender = createCandidate("msg_sender@test.com");
        receiver = createRecruiter("msg_receiver@test.com");
        senderToken = generateToken("msg_sender@test.com", "CANDIDATE");
    }

    @Test
    @DisplayName("GET /message/messages should return empty list initially")
    void getAllMessages_whenEmpty_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/message/messages", HttpMethod.GET, authHeader(senderToken), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("POST /message/{idUserSend}/{idUserReceive} should send a message")
    void sendMessage_shouldReturn201() {
        Message message = new Message();
        message.setFullName("Test User");
        message.setSubject("Hello");
        message.setMessage("This is a test message");

        ResponseEntity<Message> response = restTemplate.exchange(
                "/message/" + sender.getId() + "/" + receiver.getId(),
                HttpMethod.POST,
                authEntity(message, senderToken),
                Message.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSubject()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("GET /message/{id} should return message when found")
    void getMessageById_whenFound_shouldReturn200() {
        Message saved = messageRepository.save(Message.builder()
                .fullName("Test")
                .subject("Sub")
                .message("Body")
                .userSend(sender)
                .userReceive(receiver)
                .build());

        ResponseEntity<Message> response = restTemplate.exchange(
                "/message/" + saved.getId(),
                HttpMethod.GET,
                authHeader(senderToken),
                Message.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSubject()).isEqualTo("Sub");
    }

    @Test
    @DisplayName("GET /message/{id} should return 404 when not found")
    void getMessageById_whenNotFound_shouldReturn404() {
        ResponseEntity<Message> response = restTemplate.exchange(
                "/message/9999", HttpMethod.GET, authHeader(senderToken), Message.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE /message/{id} should delete message")
    void deleteMessage_shouldReturn200() {
        Message saved = messageRepository.save(Message.builder()
                .fullName("Test")
                .subject("To Delete")
                .message("Bye")
                .userSend(sender)
                .userReceive(receiver)
                .build());

        ResponseEntity<String> response = restTemplate.exchange(
                "/message/" + saved.getId(),
                HttpMethod.DELETE,
                authHeader(senderToken),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
        assertThat(messageRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /message/{id} should return 404 when not found")
    void deleteMessage_whenNotFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/message/9999", HttpMethod.DELETE, authHeader(senderToken), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
