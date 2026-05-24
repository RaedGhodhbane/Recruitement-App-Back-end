package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.ContactDTO;
import com.app.recruitmentapp.services.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Contact", description = "Formulaire de contact")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @Operation(summary = "Envoyer un message", description = "Envoie un message de contact depuis un utilisateur connecté")
    @PostMapping(path = "/{idUserSend}")
    public ResponseEntity<ContactDTO> sendMessageByUser(
            @RequestBody ContactDTO contactDTO, @PathVariable Long idUserSend) {
        ContactDTO saved = contactService.sendMessageContact(contactDTO, idUserSend);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
