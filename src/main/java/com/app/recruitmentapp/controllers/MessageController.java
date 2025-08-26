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

    @PostMapping(path = "/{idUserSend}/{idUserReceive}")
    public ResponseEntity<Message> sendMessageByUser(
            @RequestBody Message message, @PathVariable Long idUserSend, @PathVariable Long idUserReceive
    ) {
        Message sendMessageByUser = messageService.sendMessage(message,idUserSend,idUserReceive);
        return ResponseEntity.status(HttpStatus.CREATED).body(sendMessageByUser);
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
