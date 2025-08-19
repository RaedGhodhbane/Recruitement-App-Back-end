package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Message;
import com.app.recruitmentapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> message = messageService.getMessageById(id);
        return message.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/recruiter/{idRecruiter}")
    public ResponseEntity<Message> sendMessageByRecruiter(
            @RequestBody Message message, @PathVariable Long idRecruiter
    ) {
        Message savedMessage = messageService.sendMessageByRecruiter(message,idRecruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @PostMapping(path = "/candidate/{idCandidate}")
    public ResponseEntity<Message> sendMessageByCandidate(
            @RequestBody Message message, @PathVariable Long idCandidate
    ) {
        Message savedMessage = messageService.sendMessageByCandidate(message,idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@RequestBody Message message,@PathVariable Long id) {
        try {
            Message updatedMessage = messageService.updateMessage(id,message);
            return ResponseEntity.ok(updatedMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        try {
            messageService.deleteMessage(id);
            return ResponseEntity.ok("Message deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



}
