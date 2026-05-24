package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.ContactDTO;
import com.app.recruitmentapp.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping(path = "/{idUserSend}")
    public ResponseEntity<ContactDTO> sendMessageByUser(
            @RequestBody ContactDTO contactDTO, @PathVariable Long idUserSend) {
        ContactDTO saved = contactService.sendMessageContact(contactDTO, idUserSend);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
