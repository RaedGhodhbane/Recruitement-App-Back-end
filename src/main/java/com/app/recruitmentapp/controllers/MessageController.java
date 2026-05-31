package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.MessageDTO;
import com.app.recruitmentapp.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Messages", description = "Gestion des messages entre utilisateurs")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/message")
@PreAuthorize("isAuthenticated()")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Operation(summary = "Liste des messages", description = "Retourne tous les messages")
    @GetMapping("/messages")
    public List<MessageDTO> getAllMessages() {
        return messageService.getAllMessages();
    }

    @Operation(summary = "Détail d'un message", description = "Retourne un message par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Envoyer un message", description = "Envoie un message d'un utilisateur à un autre")
    @PostMapping(path = "/{idUserSend}/{idUserReceive}")
    public ResponseEntity<MessageDTO> sendMessageByUser(
            @Valid @RequestBody MessageDTO messageDTO, @PathVariable Long idUserSend, @PathVariable Long idUserReceive
    ) {
        MessageDTO saved = messageService.sendMessage(messageDTO, idUserSend, idUserReceive);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Supprimer un message", description = "Supprime un message par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
}
